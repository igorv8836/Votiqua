package com.example.feature.voting.data.repository

import org.example.votiqua.models.poll.Poll

interface PollRepository {
    suspend fun getMyPolls(limit: Int = 10, offset: Int = 0): Result<List<Poll>>
    suspend fun getOtherPolls(limit: Int = 10, offset: Int = 0): Result<List<Poll>>
    suspend fun getPoll(id: Int): Result<Poll>
    suspend fun createPoll(poll: Poll): Result<Poll>
    suspend fun updatePoll(poll: Poll): Result<Poll>

    suspend fun vote(pollId: Int, optionId: Int): Result<Poll>
    suspend fun toggleFavorite(pollId: Int): Result<Boolean>
    suspend fun getFavorites(): Result<List<Poll>>
}