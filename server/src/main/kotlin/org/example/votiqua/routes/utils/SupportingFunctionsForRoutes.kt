package org.example.votiqua.routes.utils

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import org.example.votiqua.network.search.common.AnswerType
import org.example.votiqua.network.search.common.BaseResponse
import org.example.votiqua.network.search.common.ErrorType

suspend fun ApplicationCall.handleSuccess(message: String = AnswerType.SUCCESS.message) {
    this.respond(HttpStatusCode.OK, BaseResponse(true, message))
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

suspend fun ApplicationCall.handleConflict(exception: Exception) {
    this.respond(
        HttpStatusCode.Conflict,
        exception.message ?: ErrorType.GENERAL.message
    )
}
