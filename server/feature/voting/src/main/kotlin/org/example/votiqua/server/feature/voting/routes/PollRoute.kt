package org.example.votiqua.server.feature.voting.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.utils.getUserId
import org.example.votiqua.server.common.utils.handleBadRequest
import org.example.votiqua.server.common.utils.receiveOrException
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.voting.data.repository.PollRepository
import org.example.votiqua.server.feature.voting.domain.usecase.PollUseCase
import org.koin.ktor.ext.inject

fun Route.pollRoute() {
    val pollUseCase by application.inject<PollUseCase>()
    val pollRepository by application.inject<PollRepository>()

    authenticate("jwt") {
        route("/polls") {
            get {
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

                val polls = pollRepository.searchPolls("", limit)
                call.respond(HttpStatusCode.OK, mapOf("count" to polls.size, "results" to polls))
            }

            post {
                val userId = call.requireAuthorization()

                val poll = call.receiveOrException<Poll>()

                val createdPoll = pollUseCase.create(
                    poll = poll,
                    authorId = userId,
                )

                call.respond(HttpStatusCode.Created, createdPoll)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: run {
                    call.handleBadRequest("Invalid poll ID")
                    return@get
                }
                val userId = call.getUserId()

                val poll = pollUseCase.get(id, userId)

                call.respond(HttpStatusCode.OK, poll)
            }

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

                val success = pollRepository.votePoll(pollId, optionId, userId)
                if (!success) {
                    call.respond(HttpStatusCode.NotFound, "Poll or option not found")
                    return@post
                }

                val updatedPoll = pollRepository.getPollById(pollId, userId)
                call.respond(HttpStatusCode.OK, updatedPoll!!)
            }

            get("/user/{userId}") {
                val targetUserId = call.parameters["userId"]?.toIntOrNull() ?: run {
                    call.handleBadRequest("Invalid user ID")
                    return@get
                }

                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

                val polls = pollRepository.getUserPolls(targetUserId, limit, offset)
                call.respond(HttpStatusCode.OK, mapOf("count" to polls.size, "results" to polls))
            }
        }
    }
} 