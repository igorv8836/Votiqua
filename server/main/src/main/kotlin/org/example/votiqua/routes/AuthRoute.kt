package org.example.votiqua.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.votiqua.network.auth.LoginRequest
import org.example.votiqua.network.auth.PasswordRecoveryRequest
import org.example.votiqua.network.auth.PasswordResetRequest
import org.example.votiqua.network.auth.RegisterRequest
import org.example.votiqua.utils.receiveOrException

fun Route.authRoute() {
    route("auth") {
        post("/login") {
            val login = call.receiveOrException<LoginRequest>()
            call.respond(HttpStatusCode.OK, mapOf("message" to "Успешный вход"))
        }
        post("/register") {
            val register = call.receiveOrException<RegisterRequest>()
            call.respond(HttpStatusCode.Created, mapOf("message" to "Пользователь зарегистрирован"))
        }
        post("/recover") {
            val recovery = call.receiveOrException<PasswordRecoveryRequest>()
            call.respond(HttpStatusCode.OK, mapOf("message" to "Письмо для восстановления пароля отправлено"))
        }
        post("/reset") {
            val reset = call.receiveOrException<PasswordResetRequest>()
            call.respond(HttpStatusCode.OK, mapOf("message" to "Пароль изменен"))
        }
    }
}