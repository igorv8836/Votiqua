package org.example.votiqua.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.models.common.ErrorType
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.HTTPForbiddenException
import org.example.votiqua.server.common.models.HTTPUnauthorizedException
import org.example.votiqua.server.common.models.IncorrectBodyException
import org.example.votiqua.server.common.models.OutOfConfigRangeException
import org.example.votiqua.server.common.utils.handleForbidden
import org.example.votiqua.server.common.utils.handleUnauthorized

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

        exception<HTTPConflictException> { call, exception ->
            call.respond(
                status = HttpStatusCode.Conflict,
                message = BaseResponse(
                    message = exception.message ?: ErrorType.GENERAL.message,
                )
            )
        }

        exception<NotFoundException> { call, exception ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = BaseResponse(
                    message = exception.message ?: ErrorType.POLL_NOT_FOUND.message,
                )
            )
        }

        exception<HTTPUnauthorizedException> { call, exception ->
            call.handleUnauthorized(exception.message)
        }

        exception<HTTPForbiddenException> { call, exception ->
            call.handleForbidden(
                message = exception.message,
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

        exception<BadRequestException> { call, exception ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = BaseResponse(
                    message = exception.message ?: ErrorType.GENERAL.message,
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