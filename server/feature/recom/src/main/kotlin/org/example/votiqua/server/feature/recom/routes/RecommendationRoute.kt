package org.example.votiqua.server.feature.recom.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.recom.data.repository.RecomRepository
import org.koin.ktor.ext.inject

fun Route.recommendationRoute() {
    val recomRepository by application.inject<RecomRepository>()
    
    route("/main") {
        authenticate("jwt") {
            get {
                val userId = call.requireAuthorization()
                val limit = minOf(call.request.queryParameters["limit"]?.toIntOrNull() ?: 10, 100)

                val mainScreenData = recomRepository.getMainScreenData(limit, userId)

                call.respond(
                    HttpStatusCode.OK,
                    mainScreenData
                )
            }
        }
    }
}