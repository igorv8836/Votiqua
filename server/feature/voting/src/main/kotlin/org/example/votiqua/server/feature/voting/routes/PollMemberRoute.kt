package org.example.votiqua.server.feature.voting.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.votiqua.models.poll.LinkRequest
import org.example.votiqua.models.poll.VoteRequest
import org.example.votiqua.server.common.utils.handleSuccess
import org.example.votiqua.server.common.utils.receiveOrException
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.voting.domain.usecase.PollMemberUseCase
import org.example.votiqua.server.feature.voting.utils.requireUserIdAndPollId
import org.koin.ktor.ext.inject

fun Route.pollMemberRoute() {
    val pollMemberUseCase by application.inject<PollMemberUseCase>()

    authenticate("jwt") {
        route("/poll-member") {
            post("/{id}/vote") {
                val data = call.requireUserIdAndPollId()
                val voteRequest = call.receive<VoteRequest>()

                pollMemberUseCase.vote(
                    pollId = data.pollId,
                    optionId = voteRequest.optionId,
                    userId = data.userId,
                )
                
                call.handleSuccess()
            }
            
            delete("/{id}/leave") {
                val data = call.requireUserIdAndPollId()

                pollMemberUseCase.leavePoll(
                    pollId = data.pollId,
                    userId = data.userId,
                )
                
                call.handleSuccess()
            }

            get("/join-by-link") {
                val link = call.receiveOrException<LinkRequest>()
                val userId = call.requireAuthorization()

                val poll = pollMemberUseCase.joinByLink(link.link, userId)
                
                call.respond(HttpStatusCode.OK, poll)
            }

            post("/{id}/join") {
                val data = call.requireUserIdAndPollId()

                pollMemberUseCase.joinToPoll(data.pollId, data.userId)

                call.handleSuccess()
            }
        }
    }
}