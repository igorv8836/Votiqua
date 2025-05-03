package org.example.votiqua.server.feature.auth.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.votiqua.models.auth.LoginRequest
import org.example.votiqua.models.auth.PasswordChangeRequest
import org.example.votiqua.models.auth.PasswordRecoveryRequest
import org.example.votiqua.models.auth.PasswordResetRequest
import org.example.votiqua.models.auth.RegisterRequest
import org.example.votiqua.models.auth.TokenResponse
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.models.common.ErrorType
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.utils.handleSuccess
import org.example.votiqua.server.common.utils.receiveOrException
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.auth.domain.usecases.PasswordResetUseCase
import org.example.votiqua.server.feature.auth.domain.usecases.UserUseCase
import org.example.votiqua.server.feature.profile.data.ProfileRepository
import org.koin.ktor.ext.inject

fun Route.authRoute() {
    val userUseCase by application.inject<UserUseCase>()
    val resetUseCase by application.inject<PasswordResetUseCase>()
    val profileRepository by application.inject<ProfileRepository>()

    route("auth") {
        post("/login") {
            val request = call.receiveOrException<LoginRequest>()

            val res = userUseCase.loginUser(
                emailRequest = request,
            )

            call.respond(HttpStatusCode.OK, TokenResponse(res))
        }

        post("/register") {
            val emailRegisterRequest = call.receiveOrException<RegisterRequest>()

            val res = userUseCase.createUser(
                user = emailRegisterRequest,
            )

            call.respond(HttpStatusCode.OK, TokenResponse(res))
        }

        post("/send_reset_code") {
            val request = call.receiveOrException<PasswordRecoveryRequest>()

            val res = resetUseCase.saveResetCode(email = request.email)

            call.handleResult(res, message = "A reset code has been sent to your email")
        }

        post("/password_reset") {
            val request = call.receiveOrException<PasswordResetRequest>()

            val res = resetUseCase.updatePassword(
                request = request,
            )

            if (res) {
                call.handleSuccess(
                    message = "Your password has been reset successfully",
                )
            } else {
                call.respond(
                    HttpStatusCode.Conflict,
                    message = BaseResponse("The provided reset code is invalid or has expired"),
                )
            }
        }

        get("/is-username-taken/{username}") {
            val username = call.parameters["username"] ?: throw BadRequestException("Username is required")
            val taken = userUseCase.isUsernameTaken(username)
            call.respond(HttpStatusCode.OK, mapOf("isUsed" to taken))
        }

        authenticate("jwt") {
            get("/check-token") {
                call.requireAuthorization()
                call.respond(HttpStatusCode.OK, BaseResponse(true))
            }

            post("/change_password") {
                val userId = call.requireAuthorization()
                val request = call.receiveOrException<PasswordChangeRequest>()

                val user = profileRepository.getUserProfile(userId)
                    ?: throw HTTPConflictException(ErrorType.USER_NOT_FOUND.message)

                userUseCase.checkAndUpdatePassword(
                    email = user.email,
                    lastPassword = request.lastPassword,
                    newPassword = request.newPassword,
                )

                call.handleSuccess(
                    message = "Your password has been reset successfully",
                )
            }
        }
    }
}

suspend fun <T> ApplicationCall.handleResult(
    res: Result<T>,
    message: String? = null,
) {
    res.fold(
        onSuccess = {
            handleSuccess(message ?: it as? String)
        },
        onFailure = {
            throw it
        },
    )
}