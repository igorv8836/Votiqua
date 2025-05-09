package com.example.feature.profile.ui.favorites_screen

import com.example.feature.voting.domain.models.PollCardState

data class FavoritePollsState(
    val polls: List<PollCardState> = emptyList(),
    val isLoading: Boolean = false,
)

sealed class FavoritePollsEffect {

}