package org.example.votiqua.server.feature.voting.domain.usecase

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.feature.voting.data.repository.PollRepository

class GetPollUseCase(
    private val pollRepository: PollRepository,
    private val favoritePollUseCase: FavoritePollUseCase,
) {
    suspend fun get(
        pollId: Int,
        userId: Int? = null,
    ) : Poll {
        val poll = getPollOrException(pollId)

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

    suspend fun getParticipatedPolls(userId: Int, limit: Int = 10, offset: Int = 0): List<Poll> {
        return pollRepository.getParticipatedPolls(userId, limit, offset)
    }

    suspend fun getPollOrException(pollId: Int): Poll {
        return pollRepository.getPollById(pollId)
            ?: throw HTTPConflictException("Poll not found")
    }
}