package org.example.votiqua.server.feature.voting.domain.usecase

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.HTTPForbiddenException
import org.example.votiqua.server.feature.voting.data.repository.PollRepository

class PollUpdateUseCase(
    private val pollRepository: PollRepository,
    private val getPollUseCase: GetPollUseCase,
    private val pollManageUseCase: PollManageUseCase,
) {

    suspend fun updatePoll(
        poll: Poll,
        userId: Int,
    ): Poll {
        val foundPoll = getPollUseCase.getPollOrException(pollId = poll.id)

        if (foundPoll.authorId != userId) {
            throw HTTPForbiddenException()
        }

        if (pollManageUseCase.isStartedPoll(foundPoll)) {
            throw HTTPForbiddenException("it is forbidden to edit after the start of voting")
        }

        val updatedPoll = pollRepository.updatePoll(poll) ?: throw HTTPConflictException()
        return getPollUseCase.fillContext(updatedPoll, userId)
    }
}