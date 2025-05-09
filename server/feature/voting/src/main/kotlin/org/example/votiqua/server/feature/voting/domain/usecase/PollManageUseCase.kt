package org.example.votiqua.server.feature.voting.domain.usecase

import io.ktor.server.plugins.BadRequestException
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.HTTPForbiddenException
import org.example.votiqua.server.feature.voting.data.repository.PollRepository

class PollManageUseCase(
    private val pollRepository: PollRepository,
    private val getPollUseCase: GetPollUseCase,
) {
    suspend fun create(
        poll: Poll,
        authorId: Int,
    ) : Poll {
        if (poll.question.isBlank() || poll.options.isEmpty()) {
            throw BadRequestException("Poll must have a question and at least one option")
        }

        val createdPoll = pollRepository.createPoll(poll, authorId)

        return createdPoll
    }

    suspend fun deletePoll(pollId: Int, userId: Int) {
        val poll = getPollUseCase.getPollOrException(pollId)

        if (poll.authorId != userId) {
            throw HTTPForbiddenException()
        }

        pollRepository.deletePoll(pollId)
    }

    suspend fun startPoll(
        pollId: Int,
        userId: Int,
    ) {
        val poll = getPollUseCase.getPollOrException(pollId)

        if (!isStartedPoll(poll) && isPollAuthor(poll, userId)) {
            pollRepository.startPoll(pollId)
        } else {
            throw HTTPConflictException("Voting has already started")
        }
    }

    suspend fun regeneratePollLink(pollId: Int, userId: Int): String {
        val poll = getPollUseCase.getPollOrException(pollId)
        
        if (poll.authorId != userId) {
            throw HTTPForbiddenException("Only the author can regenerate the poll link")
        }
        
        return pollRepository.regeneratePollLink(pollId)
    }

    suspend fun isStartedPoll(pollId: Int): Boolean {
        return getPollUseCase.getPollOrException(pollId).isStarted
    }

    fun isStartedPoll(poll: Poll): Boolean {
        return poll.isStarted
    }

    fun isPollAuthor(poll: Poll, userId: Int): Boolean {
        return poll.authorId == userId
    }
}