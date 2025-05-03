package com.example.feature.voting.data.repository

import com.example.feature.voting.data.PollRemoteDataSource
import org.example.votiqua.models.poll.Poll

class PollRepositoryImpl(
    private val remoteDataSource: PollRemoteDataSource
) : PollRepository {

    override suspend fun getPolls(limit: Int, offset: Int): Result<List<Poll>> {
        return remoteDataSource.getPolls(limit, offset)
            .map { it.results }
    }

    override suspend fun getPoll(id: Int): Result<Poll> {
        return remoteDataSource.getPoll(id)
    }

    override suspend fun createPoll(poll: Poll): Result<Poll> {
        return remoteDataSource.createPoll(poll)
    }

    override suspend fun updatePoll(poll: Poll): Result<Poll> {
        return remoteDataSource.updatePoll(poll)
    }

    override suspend fun vote(pollId: Int, optionId: Int): Result<Poll> {
        return remoteDataSource.vote(pollId, optionId)
    }

    override suspend fun getUserPolls(userId: Int, limit: Int, offset: Int): Result<List<Poll>> {
        return remoteDataSource.getUserPolls(userId, limit, offset)
            .map { it.results }
    }

    override suspend fun toggleFavorite(pollId: Int): Result<Boolean> {
        return remoteDataSource.toggleFavorite(pollId)
            .map { true }
    }

    override suspend fun getFavorites(): Result<List<Poll>> {
        return remoteDataSource.getFavorites()
    }
}