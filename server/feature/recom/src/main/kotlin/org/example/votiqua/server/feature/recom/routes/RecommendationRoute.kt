package org.example.votiqua.server.feature.recom.routes

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.example.votiqua.server.common.utils.requireAuthorization

fun Route.recommendationRoute() {
    route("/recommendations") {
        get("/polls") {
            val userId = call.requireAuthorization()
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

//            val polls = PollRepository.getRecommendedPolls(userId, limit)
//            call.respond(HttpStatusCode.OK, mapOf("count" to polls.size, "results" to polls))
        }
    }
}