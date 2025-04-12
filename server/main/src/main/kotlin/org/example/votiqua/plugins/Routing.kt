package org.example.votiqua.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.example.votiqua.routes.notificationRoute
import org.example.votiqua.routes.profileRoute
import org.example.votiqua.routes.recommendationRoute
import org.example.votiqua.routes.searchRoute
import org.example.votiqua.server.feature.auth.routes.authRoute
import org.example.votiqua.server.feature.voting.routes.pollRoute

fun Application.configureRouting(config: ApplicationConfig) {
    install(AutoHeadResponse)
    routing {
        route("api/v1"){
            searchRoute()
            authRoute()
            pollRoute()
            profileRoute()
            recommendationRoute()
            notificationRoute()
        }
    }
}
