package org.example.votiqua.server.feature.voting.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.server.common.utils.handleBadRequest
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.voting.domain.usecase.FavoritePollUseCase
import org.koin.ktor.ext.inject

fun Route.favoritePollRoute() {
    val favoritePollUseCase by application.inject<FavoritePollUseCase>()
    
    authenticate("jwt") {
        route("/favorites") {
            post("/{pollId}") {
                val userId = call.requireAuthorization()
                val pollId = call.parameters["pollId"]?.toIntOrNull() ?: run {
                    call.handleBadRequest("Invalid poll ID")
                    return@post
                }

                val isAdded = favoritePollUseCase.toggleFavorite(userId, pollId)
                val message = if (isAdded) "Poll added to favorites" else "Poll removed from favorites"
                
                call.respond(HttpStatusCode.OK, BaseResponse(message))
            }

            get {
                val userId = call.requireAuthorization()

                val polls = favoritePollUseCase.getPolls(userId)
                call.respond(HttpStatusCode.OK, mapOf("count" to polls.size, "results" to polls))
            }
        }
    }
}