package com.example.feature.voting.ui.poll_list_screen

import com.example.feature.voting.domain.models.UiPoll

data class PollListState(
    val myPolls: List<UiPoll> = emptyList(),
    val otherPolls: List<UiPoll> = emptyList(),
    val myPollsIsLoading: Boolean = false,
    val otherPollsIsLoading: Boolean = false,
)

sealed class PollListEffect {
    data class NavigateToDetails(val pollId: Int) : PollListEffect()
}