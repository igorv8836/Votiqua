package org.example.votiqua.domain.repository

import org.example.votiqua.domain.model.poll.Poll

interface PollRepository {
    suspend fun getPollById(pollId: Int): Poll?
    suspend fun updatePoll(poll: Poll)
    suspend fun deletePoll(pollId: Int)
}