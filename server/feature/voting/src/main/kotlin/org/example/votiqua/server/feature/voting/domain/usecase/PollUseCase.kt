package org.example.votiqua.server.feature.voting.domain.usecase

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPForbiddenException
import org.example.votiqua.server.feature.voting.data.repository.PollAuthorRepository
import org.example.votiqua.server.feature.voting.data.repository.PollRepository

class PollUseCase(
    private val pollRepository: PollRepository,
    private val pollAuthorRepository: PollAuthorRepository,
) {
    suspend fun create(
        poll: Poll,
        authorId: Int,
    ) : Poll {
        if (poll.question.isBlank() || poll.options.isEmpty()) {
            throw BadRequestException("Poll must have a question and at least one option")
        }

        val createdPoll = pollRepository.createPoll(poll, authorId)

        pollAuthorRepository.addPollAuthor(
            createdPoll = createdPoll,
            userId = authorId,
        )

        return createdPoll
    }

    suspend fun get(
        pollId: Int,
        userId: Int?,
    ) : Poll {
        val poll = pollRepository.getPollById(pollId, userId) ?: run {
            throw NotFoundException("Poll not found or poll is private")
        }

        if (poll.isOpen) return poll
        if (userId == null) throw HTTPForbiddenException()

        val isAccessed = pollRepository.checkAccess(
            userId = userId,
            pollId = pollId,
        )

        return poll.takeIf { isAccessed } ?: throw HTTPForbiddenException()
    }
}