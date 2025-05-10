package com.example.feature.voting.data.repository

import com.example.common.SnackbarManager
import com.example.common.handleException
import com.example.feature.voting.data.PollMemberRemoteDataSource
import com.example.feature.voting.data.PollRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.votiqua.models.poll.Poll

class PollRepositoryImpl(
    private val remoteDataSource: PollRemoteDataSource,
    private val pollMemberRemoteDataSource: PollMemberRemoteDataSource,
    private val snackbarManager: SnackbarManager,
) : PollRepository {
    private val _myPolls = MutableStateFlow<List<Poll>>(emptyList())
    override val myPolls = _myPolls.asStateFlow()
    private val _otherPolls = MutableStateFlow<List<Poll>>(emptyList())
    override val otherPolls = _otherPolls.asStateFlow()

    private suspend fun updatePolls() {
        getMyPolls().onSuccess {
            _myPolls.emit(it)
        }.handleException(snackbarManager)

        getOtherPolls().onSuccess {
            _otherPolls.emit(it)
        }.handleException(snackbarManager)
    }

    override suspend fun getMyPolls(limit: Int, offset: Int): Result<List<Poll>> {
        return remoteDataSource.getMyPolls(limit, offset)
            .map { it.polls }
    }

    override suspend fun getOtherPolls(limit: Int, offset: Int): Result<List<Poll>> {
        return remoteDataSource.getOtherPolls(limit, offset)
            .map { it.polls }
    }

    override suspend fun getPoll(id: Int): Result<Poll> {
        return remoteDataSource.getPoll(id)
    }

    override suspend fun createPoll(poll: Poll): Result<Poll> {
        return remoteDataSource.createPoll(poll).also {
            updatePolls()
        }
    }

    override suspend fun updatePoll(poll: Poll): Result<Poll> {
        return remoteDataSource.updatePoll(poll).also {
            updatePolls()
        }
    }

    override suspend fun regeneratePollLink(pollId: Int): Result<String> {
        return remoteDataSource.regenerateLink(pollId).map { it.link }
    }

    override suspend fun toggleFavorite(pollId: Int): Result<Boolean> {
        return remoteDataSource.toggleFavorite(pollId)
            .map { true }
    }

    override suspend fun getFavorites(): Result<List<Poll>> {
        return remoteDataSource.getFavorites().map { it.polls }
    }

    override suspend fun deletePoll(pollId: Int): Result<Unit> {
        return remoteDataSource.deletePoll(pollId = pollId).also {
            updatePolls()
        }
    }

    override suspend fun startPoll(pollId: Int): Result<Unit> {
        return remoteDataSource.startPoll(pollId).also {
            updatePolls()
        }
    }

    override suspend fun vote(pollId: Int, optionId: Int): Result<Unit> {
        return pollMemberRemoteDataSource.vote(pollId, optionId)
    }

    override suspend fun leaveFromPoll(pollId: Int): Result<Unit> {
        return pollMemberRemoteDataSource.leavePoll(pollId)
    }

    override suspend fun joinByLink(link: String): Result<Poll> {
        return pollMemberRemoteDataSource.joinByLink(link)
    }

    override suspend fun joinByButton(pollId: Int): Result<Unit> {
        return pollMemberRemoteDataSource.joinToPoll(pollId)
    }
}