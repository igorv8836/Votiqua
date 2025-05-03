package org.example.votiqua.presentation.polls.favorites

import org.example.votiqua.models.poll.Poll

data class FavoritePollsState(
    val polls: List<Poll> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class FavoritePollsEffect {
    data class NavigateToDetails(val pollId: Int) : FavoritePollsEffect()
    data class ShowError(val message: String) : FavoritePollsEffect()
} 