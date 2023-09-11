package com.jetbrains.shared

import app.cash.turbine.test
import com.jetbrains.kmmktor2.GreetingClient
import com.jetbrains.kmmktor2.Hello
import com.jetbrains.kmmktor2.MainActivityViewModel
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityViewModelTests {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @BeforeTest
    fun setUpDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun greetingTest() = runTest {
        val helloString = "Yello"
        val hello = Hello("Yello")

        val mockEngine = MockEngine { respondJson(listOf(hello)) }
        val greetingClient = GreetingClient(mockEngine)
        val testObject = MainActivityViewModel(greetingClient)

        testObject.greetingFlow.test {
            advanceUntilIdle()
            expectNoEvents()

            testObject.fetchGreeting()

            assertEquals(helloString, awaitItem())
        }
    }

    @Test
    fun gresretingTest() = runTest {
        val helloString = "Yello"
        val hello = Hello("Yello")

        val mockEngine = MockEngine { respondJson(listOf(hello)) }
        val greetingClient = GreetingClient(mockEngine)
        val testObject = MainActivityViewModel(greetingClient)

        testObject.greetingFlow.test {
            advanceUntilIdle()
            expectNoEvents()

            testObject.fetchGreeting()

            assertEquals(helloString, awaitItem())
        }
    }

    @Test
    fun greetingTaest() = runTest {
        val helloString = "Yello"
        val hello = Hello("Yello")

        val mockEngine = MockEngine { respondJson(listOf(hello)) }
        val greetingClient = GreetingClient(mockEngine)
        val testObject = MainActivityViewModel(greetingClient)

        testObject.greetingFlow.test {
            advanceUntilIdle()
            expectNoEvents()

            testObject.fetchGreeting()

            assertEquals(helloString, awaitItem())
        }
    }

    @Test
    fun greetingTesst() = runTest {
        val helloString = "Yello"
        val hello = Hello("Yello")

        val mockEngine = MockEngine { respondJson(listOf(hello)) }
        val greetingClient = GreetingClient(mockEngine)
        val testObject = MainActivityViewModel(greetingClient)

        testObject.greetingFlow.test {
            advanceUntilIdle()
            expectNoEvents()

            testObject.fetchGreeting()

            assertEquals(helloString, awaitItem())
        }
    }

    @Test
    fun greetifngTest() = runTest {
        val helloString = "Yello"
        val hello = Hello("Yello")

        val mockEngine = MockEngine { respondJson(listOf(hello)) }
        val greetingClient = GreetingClient(mockEngine)
        val testObject = MainActivityViewModel(greetingClient)

        testObject.greetingFlow.test {
            advanceUntilIdle()
            expectNoEvents()

            testObject.fetchGreeting()

            assertEquals(helloString, awaitItem())
        }
    }
}

inline fun <reified T> MockRequestHandleScope.respondJson(
    type: T,
    status: HttpStatusCode = HttpStatusCode.OK,
    headers: Headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
): HttpResponseData =
    respond(ByteReadChannel(Json.encodeToString(serializer(), type).toByteArray(Charsets.UTF_8)), status, headers)
