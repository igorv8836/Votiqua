package org.example.votiqua.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.models.common.ErrorType
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.HTTPUnauthorizedException
import org.example.votiqua.server.common.models.IncorrectBodyException
import org.example.votiqua.server.common.models.OutOfConfigRangeException

fun Application.configureStatusPages() {
    install(StatusPages) {
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

        exception<HTTPConflictException> { call, exception ->
            call.respond(
                status = HttpStatusCode.Conflict,
                message = BaseResponse(
                    message = exception.message ?: ErrorType.GENERAL.message,
                )
            )
        }

        exception<HTTPUnauthorizedException> { call, exception ->
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = BaseResponse(
                    message = exception.message ?: ErrorType.GENERAL.message,
                )
            )
        }

        exception<OutOfConfigRangeException> { call, exception ->
            call.respond(
                status = HttpStatusCode.UnprocessableEntity,
                message = BaseResponse(
                    message = exception.message ?: ErrorType.GENERAL.message,
                )
            )
        }
    }
}