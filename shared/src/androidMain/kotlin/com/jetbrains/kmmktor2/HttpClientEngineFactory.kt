package com.jetbrains.kmmktor2

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual val defaultEngineFactory: HttpClientEngineFactory<*> = OkHttp