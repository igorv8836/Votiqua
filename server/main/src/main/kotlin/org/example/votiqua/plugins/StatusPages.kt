package org.example.votiqua.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.example.votiqua.network.search.common.BaseResponse
import org.example.votiqua.network.search.common.ErrorType
import org.example.votiqua.utils.IncorrectBodyException

fun Application.configureStatusPages() {
    install(StatusPages) {

        status(HttpStatusCode.NotFound) { call, _ ->
            call.respondRedirect("")
        }

        exception<IncorrectBodyException> { call, exception ->
            call.respond(
                HttpStatusCode.BadRequest,
                BaseResponse(
                    message = exception.message ?: ErrorType.INCORRECT_BODY.message,
                )
            )
        }

        exception<Exception> { call, exception ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = BaseResponse(
                    message = exception.message ?: ErrorType.GENERAL.message,
                )
            )
        }
    }
}