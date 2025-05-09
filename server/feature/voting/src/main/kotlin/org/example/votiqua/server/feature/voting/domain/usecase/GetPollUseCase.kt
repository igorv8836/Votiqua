package org.example.votiqua.server.feature.voting.domain.usecase

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.feature.voting.data.repository.PollRepository

class GetPollUseCase(
    private val pollRepository: PollRepository,
    private val favoritePollUseCase: Lazy<FavoritePollUseCase>,
) {
    suspend fun get(
        pollId: Int,
        userId: Int? = null,
    ) : Poll {
        val poll = getPollOrException(pollId)

        return if (userId != null) {
            fillContext(poll, userId)
        } else {
            poll
        }
    }

    suspend fun getPolls(query: String = "", limit: Int = 10, offset: Int = 0): List<Poll> {
        return pollRepository.searchPolls(query, limit, offset)
    }

    suspend fun getUserPolls(userId: Int, limit: Int = 10, offset: Int = 0): List<Poll> {
        return pollRepository.getUserPolls(userId, limit, offset).map {
            fillContext(it, userId)
        }
    }

    suspend fun getParticipatedPolls(userId: Int, limit: Int = 10, offset: Int = 0): List<Poll> {
        return pollRepository.getParticipatedPolls(userId, limit, offset).map {
            fillContext(it, userId)
        }
    }

    suspend fun getPollOrException(pollId: Int): Poll {
        return pollRepository.getPollById(pollId)
            ?: throw HTTPConflictException("Poll not found")
    }

    suspend fun fillContext(poll: Poll, userId: Int): Poll {
        val isFavorite = favoritePollUseCase.value.isFavorite(userId, poll.id)
        return poll.copy(
            context = poll.context.copy(
                isAdmin = userId == poll.authorId,
                selectedOption = poll.members.firstOrNull { it.user.id == userId }?.optionId,
                totalVotes = poll.members.count { it.voted },
                memberCount = poll.members.size,
                isFavorite = isFavorite,
            ),
        )
    }
}