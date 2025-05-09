package org.example.votiqua.server.feature.voting.utils

import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.BadRequestException
import org.example.votiqua.server.common.utils.requireAuthorization

data class UserAndPoll(
    val userId: Int,
    val pollId: Int,
)

suspend fun ApplicationCall.requireUserIdAndPollId(): UserAndPoll {
    val pollId = parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Invalid poll ID")
    val userId = this.requireAuthorization()

    return UserAndPoll(
        userId = userId,
        pollId = pollId,
    )
}