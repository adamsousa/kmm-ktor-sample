package com.jetbrains.kmmktor2

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.*

actual val defaultEngineFactory: HttpClientEngineFactory<*> = Darwin