package org.example.votiqua.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.votiqua.network.auth.LoginRequest
import org.example.votiqua.network.auth.PasswordRecoveryRequest
import org.example.votiqua.network.auth.PasswordResetRequest
import org.example.votiqua.network.auth.RegisterRequest

fun Route.authRoute() {
    route("auth") {
        post("/login") {
            val login = call.receive<LoginRequest>()
            call.respond(HttpStatusCode.OK, mapOf("message" to "Успешный вход"))
        }
        post("/register") {
            val register = call.receive<RegisterRequest>()
            call.respond(HttpStatusCode.Created, mapOf("message" to "Пользователь зарегистрирован"))
        }
        post("/recover") {
            val recovery = call.receive<PasswordRecoveryRequest>()
            call.respond(HttpStatusCode.OK, mapOf("message" to "Письмо для восстановления пароля отправлено"))
        }
        post("/reset") {
            val reset = call.receive<PasswordResetRequest>()
            call.respond(HttpStatusCode.OK, mapOf("message" to "Пароль изменен"))
        }
    }
}