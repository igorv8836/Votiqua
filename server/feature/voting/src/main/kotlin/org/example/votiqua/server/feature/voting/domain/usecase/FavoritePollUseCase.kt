package org.example.votiqua.server.feature.voting.domain.usecase

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.feature.voting.data.repository.PollFavoriteRepository
import org.example.votiqua.server.feature.voting.data.repository.PollRepository

class FavoritePollUseCase(
    private val favoriteRepository: PollFavoriteRepository,
    private val pollRepository: PollRepository,
) {
    suspend fun toggleFavorite(userId: Int, pollId: Int): Boolean {
        return favoriteRepository.changeFavoriteStatus(userId, pollId)
    }

    suspend fun getPolls(userId: Int): List<Poll> {
        return favoriteRepository.getFavoritePollsIds(userId).mapNotNull {
            pollRepository.getPollById(id = it)
        }
    }
    
    suspend fun isFavorite(userId: Int, pollId: Int): Boolean {
        return favoriteRepository.getFavoritePollsIds(userId).contains(pollId)
    }
}