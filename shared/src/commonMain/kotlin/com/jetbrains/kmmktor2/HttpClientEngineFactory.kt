package com.jetbrains.kmmktor2

import io.ktor.client.engine.*

expect val defaultEngineFactory: HttpClientEngineFactory<*>