package com.jetbrains.kmmktor2

import com.jetbrains.kmmktor2.ktor.ApiClient
import com.jetbrains.kmmktor2.ktor.HttpResponse
import com.jetbrains.kmmktor2.ktor.RequestConfig
import com.jetbrains.kmmktor2.ktor.RequestMethod
import com.jetbrains.kmmktor2.ktor.wrap
import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.Serializable

@Serializable
data class Hello(
    val string: String,
)
class GreetingClient(
    httpClientEngine: HttpClientEngine? = null,
): ApiClient(BASE_URL, httpClientEngine, JSON_DEFAULT) {

    suspend fun getHello(): HttpResponse<List<Hello>> {
        val localVariableAuthNames = listOf("bearerAuth")

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()

        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/gh/KaterinaPetrova/greeting@main/greetings.json",
            query = localVariableQuery,
            headers = localVariableHeaders
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }
}