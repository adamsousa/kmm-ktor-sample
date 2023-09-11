package com.jetbrains.kmmktor2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel

actual interface CoroutineViewModelInterface {
    fun dispose()
}

actual abstract class CoroutineViewModel : ViewModel(), CoroutineViewModelInterface {
    protected actual val coroutineScope = viewModelScope

    actual override fun dispose() {
        coroutineScope.cancel()
        onCleared()
    }

    actual override fun onCleared() {
        super.onCleared()
    }
}