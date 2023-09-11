package com.jetbrains.kmmktor2

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

actual interface CoroutineViewModelInterface {
    fun <T> observeFlowInCoroutineScope(flow: Flow<T>, onChange: (T) -> Unit, onError: ((Throwable) -> Unit)? = null) : Job
    fun dispose()
}

actual abstract class CoroutineViewModel : CoroutineViewModelInterface {
    protected actual val coroutineScope = MainScope()

    actual override fun dispose() {
        onCleared()
        coroutineScope.cancel()
    }

    protected actual open fun onCleared() {
    }

    override fun <T> observeFlowInCoroutineScope(flow: Flow<T>, onChange: (T) -> Unit, onError: ((Throwable) -> Unit)?) : Job {
        onError?.let { errorHandler ->
            return flow.onEach {
                onChange(it)
            }.catch { e ->
                errorHandler(e)
            }.launchIn(coroutineScope)
        } ?: let {
            return flow.onEach {
                onChange(it)
            }.launchIn(coroutineScope)
        }
    }
}