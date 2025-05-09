package com.example.feature.voting.domain

import com.example.feature.voting.domain.models.PollCardState
import org.example.votiqua.models.poll.Poll

interface PollCardMapper {
    fun mapToState(poll: Poll): PollCardState
    fun mapToState(polls: List<Poll>): List<PollCardState>
}