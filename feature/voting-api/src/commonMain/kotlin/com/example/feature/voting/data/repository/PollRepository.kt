package com.example.feature.voting.data.repository

import kotlinx.coroutines.flow.Flow
import org.example.votiqua.models.poll.Poll

interface PollRepository {
    val myPolls: Flow<List<Poll>>
    val otherPolls: Flow<List<Poll>>

    suspend fun getMyPolls(limit: Int = 10, offset: Int = 0): Result<List<Poll>>
    suspend fun getOtherPolls(limit: Int = 10, offset: Int = 0): Result<List<Poll>>
    suspend fun getPoll(id: Int): Result<Poll>
    suspend fun createPoll(poll: Poll): Result<Poll>
    suspend fun updatePoll(poll: Poll): Result<Poll>
    suspend fun regeneratePollLink(pollId: Int): Result<String>

    suspend fun toggleFavorite(pollId: Int): Result<Boolean>
    suspend fun getFavorites(): Result<List<Poll>>

    suspend fun deletePoll(pollId: Int): Result<Unit>
    suspend fun startPoll(pollId: Int): Result<Unit>

    suspend fun vote(pollId: Int, optionId: Int): Result<Unit>
    suspend fun leaveFromPoll(pollId: Int): Result<Unit>
    suspend fun joinByLink(link: String): Result<Poll>
}