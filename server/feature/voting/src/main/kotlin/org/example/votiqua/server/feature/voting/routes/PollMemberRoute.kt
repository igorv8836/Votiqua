package org.example.votiqua.server.feature.voting.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import org.example.votiqua.server.common.utils.handleBadRequest
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.voting.domain.usecase.PollManageUseCase
import org.koin.ktor.ext.inject

fun Route.pollMemberRoute() {
    val pollUseCase by application.inject<PollManageUseCase>()

    post("/{pollId}/vote") {
        val userId = call.requireAuthorization()

        val pollId = call.parameters["pollId"]?.toIntOrNull() ?: run {
            call.handleBadRequest("Invalid poll ID")
            return@post
        }

        val optionId = call.request.queryParameters["optionId"]?.toIntOrNull() ?: run {
            call.handleBadRequest("Option ID is required")
            return@post
        }

        val updatedPoll = pollUseCase.vote(pollId, optionId, userId)
        call.respond(HttpStatusCode.OK, updatedPoll)
    }
}