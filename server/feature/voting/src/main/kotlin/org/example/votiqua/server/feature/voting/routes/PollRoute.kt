package org.example.votiqua.server.feature.voting.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.models.poll.PollsResponse
import org.example.votiqua.models.poll.RegenerateLinkResponse
import org.example.votiqua.server.common.utils.getUserId
import org.example.votiqua.server.common.utils.handleBadRequest
import org.example.votiqua.server.common.utils.handleSuccess
import org.example.votiqua.server.common.utils.receiveOrException
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.voting.domain.usecase.GetPollUseCase
import org.example.votiqua.server.feature.voting.domain.usecase.PollManageUseCase
import org.example.votiqua.server.feature.voting.domain.usecase.PollUpdateUseCase
import org.koin.ktor.ext.inject

fun Route.pollRoute() {
    val pollUseCase by application.inject<PollManageUseCase>()
    val getPollUseCase by application.inject<GetPollUseCase>()
    val pollUpdateUseCase by application.inject<PollUpdateUseCase>()

    authenticate("jwt") {
        route("/polls") {
            get("/my-polls") {
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10000
                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
                
                val userId = call.requireAuthorization()
                val polls = getPollUseCase.getUserPolls(
                    userId = userId,
                    limit = limit,
                    offset = offset,
                )
                
                call.respond(HttpStatusCode.OK, PollsResponse(polls))
            }

            get("/other-polls") {
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10000
                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

                val userId = call.requireAuthorization()
                val polls = getPollUseCase.getParticipatedPolls(
                    userId = userId,
                    limit = limit,
                    offset = offset,
                )

                call.respond(HttpStatusCode.OK, PollsResponse(polls))
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

            post("/edit") {
                val userId = call.requireAuthorization()
                val poll = call.receiveOrException<Poll>()

                val createdPoll = pollUpdateUseCase.updatePoll(
                    poll = poll,
                    userId = userId,
                )

                call.respond(HttpStatusCode.Created, createdPoll)
            }

            delete("/{id}") {
                val data = call.requireUserIdAndPollId()

                pollUseCase.deletePoll(
                    pollId = data.pollId,
                    userId = data.userId,
                )
            }

            post("/{id}/start") {
                val data = call.requireUserIdAndPollId()

                pollUseCase.startPoll(
                    pollId = data.pollId,
                    userId = data.userId,
                )
                call.handleSuccess()
            }

            post("/{id}/regenerate-link") {
                val data = call.requireUserIdAndPollId()
                
                val newLink = pollUseCase.regeneratePollLink(
                    pollId = data.pollId,
                    userId = data.userId,
                )
                
                call.respond(
                    HttpStatusCode.OK,
                    RegenerateLinkResponse(
                        link = newLink,
                        pollId = data.pollId,
                    )
                )
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: run {
                    call.handleBadRequest("Invalid poll ID")
                    return@get
                }
                val userId = call.getUserId()

                val poll = getPollUseCase.get(id, userId)

                call.respond(HttpStatusCode.OK, poll)
            }

//            get("/user/{userId}") {
//                val targetUserId = call.parameters["userId"]?.toIntOrNull() ?: run {
//                    call.handleBadRequest("Invalid user ID")
//                    return@get
//                }
//
//                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
//                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
//
//                val polls = pollUseCase.getUserPolls(targetUserId, limit, offset)
//                call.respond(HttpStatusCode.OK, mapOf("count" to polls.size, "results" to polls))
//            }
        }
    }
}

data class UserAndPoll(
    val userId: Int,
    val pollId: Int,
)

private suspend fun ApplicationCall.requireUserIdAndPollId(): UserAndPoll {
    val pollId = parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Invalid poll ID")
    val userId = this.requireAuthorization()

    return UserAndPoll(
        userId = userId,
        pollId = pollId,
    )
}