package com.example.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SnackbarManager {
    private val _messages = MutableSharedFlow<SnackbarMessage>()
    val messages: SharedFlow<SnackbarMessage> = _messages.asSharedFlow()

    suspend fun sendMessage(message: SnackbarMessage) {
        _messages.emit(message)
    }

    suspend fun sendMessage(message: String?) {
        _messages.emit(SnackbarMessage(message))
    }
}

data class SnackbarMessage(
    val message: String?,
    val duration: SnackbarDuration = SnackbarDuration.Short
)

enum class SnackbarDuration {
    Short,
    Long,
    Indefinite
}