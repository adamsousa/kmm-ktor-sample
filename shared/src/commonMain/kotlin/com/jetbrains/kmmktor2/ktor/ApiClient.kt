package com.jetbrains.kmmktor2.ktor

import com.jetbrains.kmmktor2.defaultEngineFactory
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.content.OutgoingContent
import io.ktor.http.encodeURLQueryComponent
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.serialization.ContentConverter
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import kotlinx.serialization.json.Json

open class ApiClient(
    private val baseUrl: String,
    httpClientEngine: HttpClientEngine?,
    private val json: Json,
    private val serializer: ContentConverter = KotlinxSerializationConverter(json)
) {
    private val httpClientConfig:(HttpClientConfig<*>) -> Unit = {
        it.install(ContentNegotiation) {
            register(ContentType.Application.Json, serializer)
        }
        it.install(Logging) {
            logger = CustomHttpLogger()
            level = LogLevel.BODY
        }
    }

    private val client: HttpClient by lazy {
        httpClientConfig.let { config ->
            httpClientEngine?.let { engine ->
                HttpClient(engine, config)
            } ?: HttpClient(defaultEngineFactory, config)
        }
    }

    private val authentications: Map<String, Authentication> by lazy {
        mapOf("bearerAuth" to HttpBearerAuth("bearer"))
    }

    companion object {
        const val BASE_URL = "https://cdn.jsdelivr.net"
        val JSON_DEFAULT = Json { ignoreUnknownKeys = true }
        protected val UNSAFE_HEADERS = listOf(HttpHeaders.ContentType)
    }

    protected suspend fun <T: Any?> request(requestConfig: RequestConfig<T>, body: OutgoingContent? = EmptyContent, authNames: List<String>): HttpResponse {
        requestConfig.updateForAuth(authNames)
        val headers = requestConfig.headers

        return client.request {
            this.url {
                this.takeFrom(URLBuilder(baseUrl))
                appendPath(requestConfig.path.trimStart('/').split('/'))
                requestConfig.query.forEach { query ->
                    query.value.forEach { value ->
                        parameter(query.key, value)
                    }
                }
            }
            this.method = requestConfig.method.httpMethod
            headers.filter { header -> !UNSAFE_HEADERS.contains(header.key) }.forEach { header -> this.header(header.key, header.value) }
            if (requestConfig.method in listOf(RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH)){
                setBody(body)
            }
        }
    }

    private fun <T: Any?> RequestConfig<T>.updateForAuth(authNames: List<String>) {
        for (authName in authNames) {
            val auth = authentications[authName] ?: throw Exception("Authentication undefined: $authName")
            auth.apply(query, headers)
        }
    }

    private fun URLBuilder.appendPath(components: List<String>): URLBuilder = apply {
        encodedPath = encodedPath.trimEnd('/') + components.joinToString("/", prefix = "/") { it.encodeURLQueryComponent() }
    }

    private val RequestMethod.httpMethod: HttpMethod
        get() = when (this) {
            RequestMethod.DELETE -> HttpMethod.Delete
            RequestMethod.GET -> HttpMethod.Get
            RequestMethod.HEAD -> HttpMethod.Head
            RequestMethod.PATCH -> HttpMethod.Patch
            RequestMethod.PUT -> HttpMethod.Put
            RequestMethod.POST -> HttpMethod.Post
            RequestMethod.OPTIONS -> HttpMethod.Options
        }
}

class CustomHttpLogger: Logger {
    override fun log(message: String) {
        println("ktor: $message")
    }
}