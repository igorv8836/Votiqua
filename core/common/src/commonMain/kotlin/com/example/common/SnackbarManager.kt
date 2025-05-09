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

suspend fun <T> Result<T>.handleException(
    snackbarManager: SnackbarManager,
    fallbackMessage: String? = null,
    customMessage: (Throwable) -> String? = { null },
    action: suspend () -> Unit = { },
): Result<T> {
    exceptionOrNull()?.let {
        snackbarManager.sendMessage(customMessage(it) ?: it.message ?: fallbackMessage)
        action()
    }
    return this
}

interface ResultExceptionHandler {
    val snackbarManager: SnackbarManager

    suspend fun <T> Result<T>.handleException(
        fallbackMessage: String? = null,
        customMessage: (Throwable) -> String? = { null },
        action: suspend () -> Unit = { },
    ): Result<T> {
        return handleException(
            snackbarManager = snackbarManager,
            customMessage = customMessage,
            fallbackMessage = fallbackMessage,
            action = action,
        )
    }
}