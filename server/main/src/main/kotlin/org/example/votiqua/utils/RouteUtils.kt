package org.example.votiqua.utils

import io.ktor.server.application.*
import io.ktor.server.request.*
import org.example.votiqua.network.search.common.ErrorType

suspend inline fun <reified T> ApplicationCall.receiveOrException(): T {
    try {
        return receive()
    } catch (e: Exception) {
        throw IncorrectBodyException(ErrorType.INCORRECT_BODY.message + ": required ${T::class.simpleName}")
    }
}