package org.example.votiqua.server.feature.voting.domain.usecase

import io.ktor.server.plugins.BadRequestException
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.HTTPForbiddenException
import org.example.votiqua.server.feature.voting.data.repository.PollRepository

class PollUpdateUseCase(
    private val pollRepository: PollRepository,
) {

    suspend fun updatePoll(
        poll: Poll,
        userId: Int,
    ): Poll {
        val foundPoll = pollRepository.getPollById(poll.id) ?: throw BadRequestException("Poll not found")

        if (foundPoll.authorId != userId) {
            throw HTTPForbiddenException()
        }

        return pollRepository.updatePoll(poll) ?: throw HTTPConflictException()
    }
}