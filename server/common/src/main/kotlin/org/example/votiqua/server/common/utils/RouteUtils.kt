package org.example.votiqua.server.common.utils

import io.ktor.server.application.*
import io.ktor.server.request.*
import org.example.votiqua.models.common.ErrorType
import org.example.votiqua.server.common.models.IncorrectBodyException

suspend inline fun <reified T> ApplicationCall.receiveOrException(): T {
    try {
        return receive()
    } catch (e: Exception) {
        throw IncorrectBodyException(ErrorType.INCORRECT_BODY.message + ": required ${T::class.simpleName}")
    }
}