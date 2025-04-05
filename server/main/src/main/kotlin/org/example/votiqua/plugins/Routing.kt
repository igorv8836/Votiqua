package org.example.votiqua.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.routing.*
import org.example.votiqua.routes.searchRoute
import org.example.votiqua.server.feature.auth.routes.authRoute

fun Application.configureRouting(config: ApplicationConfig) {
    install(AutoHeadResponse)
    routing {
        route("api/v1"){
            searchRoute()
            authRoute()
        }
    }
}
