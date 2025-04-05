package org.example.votiqua.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.example.votiqua.network.search.PollSearchResponse
import org.example.votiqua.repositories.PollRepository
import org.example.votiqua.routes.utils.handleBadRequest

fun Route.searchRoute() {
    route("search") {
        get {
            val query = call.queryParameters["query"] ?: run {
                call.handleBadRequest()
                return@get
            }
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

            if (limit > 100) {
                call.handleBadRequest("Limit cannot be greater than 100")
                return@get
            }

            val pollTitles = PollRepository.searchPollTitles(query, limit)
            call.respond(HttpStatusCode.OK, PollSearchResponse(query, pollTitles.size, pollTitles))
        }
    }
}
