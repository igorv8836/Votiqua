package org.example.votiqua.server.feature.voting.domain.usecase

import io.ktor.server.plugins.BadRequestException
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.feature.voting.data.repository.PollRepository

class PollUseCase(
    private val pollRepository: PollRepository,
    private val favoritePollUseCase: FavoritePollUseCase
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

    suspend fun get(
        pollId: Int,
        userId: Int? = null,
    ) : Poll {
        val poll = pollRepository.getPollById(pollId)
            ?: throw HTTPConflictException("Poll not found")
            
        return if (userId != null) {
            val isFavorite = favoritePollUseCase.isFavorite(userId, pollId)
            poll.copy(isFavorite = isFavorite)
        } else {
            poll
        }
    }
    
    suspend fun getPolls(query: String = "", limit: Int = 10, offset: Int = 0): List<Poll> {
        return pollRepository.searchPolls(query, limit, offset)
    }
    
    suspend fun getUserPolls(userId: Int, limit: Int = 10, offset: Int = 0): List<Poll> {
        return pollRepository.getUserPolls(userId, limit, offset)
    }
    
    suspend fun vote(pollId: Int, optionId: Int, userId: Int): Poll {
        val success = pollRepository.votePoll(pollId, optionId, userId)
        if (!success) {
            throw HTTPConflictException("Poll or option not found")
        }
        
        return get(pollId, userId)
    }
}