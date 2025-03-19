package org.example.votiqua.plugins

import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity() {
    authentication {
        jwt("jwt") {
            realm = "Service server"
            validate {

            }
        }
    }
}