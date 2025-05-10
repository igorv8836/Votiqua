package org.example.votiqua.server.feature.recom.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.example.votiqua.models.search.PollSearchResponse
import org.example.votiqua.models.search.PollTitlesSearchResponse
import org.example.votiqua.server.common.utils.handleBadRequest
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.voting.domain.usecase.GetPollUseCase
import org.koin.ktor.ext.inject

fun Route.searchRoute() {
    val getPollUseCase by application.inject<GetPollUseCase>()

    route("search") {
        get {
            val query = call.request.queryParameters["query"] ?: run {
                call.handleBadRequest()
                return@get
            }
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

            if (limit > 100) {
                call.handleBadRequest("Limit cannot be greater than 100")
                return@get
            }

            val pollTitles = getPollUseCase.searchPollTitles(query, limit)
            call.respond(HttpStatusCode.OK, PollTitlesSearchResponse(query, pollTitles.size, pollTitles))
        }


        authenticate("jwt") {
            get("/polls") {
                val query = call.request.queryParameters["query"] ?: run {
                    call.handleBadRequest()
                    return@get
                }
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

                if (limit > 100) {
                    call.handleBadRequest("Limit cannot be greater than 100")
                    return@get
                }

                val userId = call.requireAuthorization()

                val polls = getPollUseCase.searchPolls(userId, query, limit)
                call.respond(HttpStatusCode.OK, PollSearchResponse(query, polls.size, polls))
            }
        }
    }
}
