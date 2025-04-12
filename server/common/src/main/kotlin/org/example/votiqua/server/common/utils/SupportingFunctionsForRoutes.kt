package org.example.votiqua.server.common.utils

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import org.example.votiqua.models.common.AnswerType
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.models.common.ErrorType

suspend fun ApplicationCall.handleSuccess(message: String? = null) {
    val answer = message ?: AnswerType.SUCCESS.message
    this.respond(HttpStatusCode.OK, BaseResponse(answer))
}

suspend fun ApplicationCall.handleUnauthorized(
    message: String? = null,
) {
    this.respond(
        HttpStatusCode.Unauthorized,
        BaseResponse(message ?: ErrorType.UNAUTHORIZED.message),
    )
}

suspend fun ApplicationCall.handleForbidden() {
    this.respond(
        HttpStatusCode.Forbidden,
        BaseResponse(ErrorType.FORBIDDEN.message)
    )
}

suspend fun ApplicationCall.handleBadRequest(message: String = ErrorType.GENERAL.message) {
    this.respond(HttpStatusCode.BadRequest, BaseResponse(message))
}

suspend fun ApplicationCall.handleConflict(exception: Throwable) {
    this.respond(
        HttpStatusCode.Conflict,
        BaseResponse(exception.message ?: ErrorType.GENERAL.message)
    )
}
