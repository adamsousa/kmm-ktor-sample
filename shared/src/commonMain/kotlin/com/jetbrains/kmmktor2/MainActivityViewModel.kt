package com.jetbrains.kmmktor2

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val greetingClient: GreetingClient = GreetingClient()
): CoroutineViewModel() {

    private val _greetingFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val greetingFlow: SharedFlow<String> = _greetingFlow

    fun fetchGreeting() {
        coroutineScope.launch {
            try {
                greetingClient.getHello().also {
                    _greetingFlow.emit(it.body().random().string)
                }
            } catch (e: Throwable) {
                _greetingFlow.emit(e.message ?: "Unknown Error")
            }
        }
    }
}