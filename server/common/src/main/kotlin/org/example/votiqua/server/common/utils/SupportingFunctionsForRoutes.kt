package org.example.votiqua.server.common.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.example.votiqua.models.common.AnswerType
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.models.common.ErrorType

suspend fun ApplicationCall.handleSuccess(message: String? = null) {
    val answer = message ?: AnswerType.SUCCESS.message
    this.respond(HttpStatusCode.OK, BaseResponse(answer))
}

suspend fun ApplicationCall.handleUnauthorized() {
    this.respond(
        HttpStatusCode.Unauthorized,
        ErrorType.UNAUTHORIZED.message
    )
}

suspend fun ApplicationCall.handleForbidden() {
    this.respond(
        HttpStatusCode.Forbidden,
        ErrorType.FORBIDDEN.message
    )
}

suspend fun ApplicationCall.handleBadRequest(message: String = ErrorType.GENERAL.message) {
    this.respond(HttpStatusCode.BadRequest, message)
}

suspend fun ApplicationCall.handleConflict(exception: Throwable) {
    this.respond(
        HttpStatusCode.Conflict,
        exception.message ?: ErrorType.GENERAL.message
    )
}
