package org.example.votiqua.server.feature.voting.domain.usecase

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.HTTPUnauthorizedException
import org.example.votiqua.server.common.utils.currentTimestamp
import org.example.votiqua.server.feature.voting.data.repository.PollParticipantRepository
import org.example.votiqua.server.feature.voting.data.repository.PollRepository

class PollMemberUseCase(
    private val pollRepository: PollRepository,
    private val getPollUseCase: GetPollUseCase,
    private val pollParticipantRepository: PollParticipantRepository
) {
    suspend fun vote(pollId: Int, optionId: Int, userId: Int) {
        val poll = getPollUseCase.getPollOrException(pollId)

        val hasAccess = checkAccessForVoting(userId, poll)
        if (!hasAccess) {
            throw HTTPUnauthorizedException("You don't have access to this poll")
        }

        pollRepository.votePoll(pollId, optionId, userId)
    }

    suspend fun leavePoll(pollId: Int, userId: Int) {
        val hasAccess = pollRepository.checkAccess(userId, pollId)
        if (!hasAccess) {
            throw HTTPUnauthorizedException("You are not a member of this poll")
        }

        pollParticipantRepository.removeMember(pollId, userId)
    }

    suspend fun joinByLink(link: String, userId: Int): Poll {
        val poll = pollRepository.findByLink(link)
            ?: throw HTTPConflictException("Poll not found")

        pollParticipantRepository.insert(userId, poll.id)

        return getPollUseCase.getPollOrException(poll.id)
    }

    private fun checkAccessForVoting(userId: Int, poll: Poll): Boolean {
        if (poll.members.firstOrNull { it.user.id == userId } == null ) {
            return false
        }

        val currentTime = currentTimestamp()

        return poll.isStarted && (poll.startTime ?: 0L) < currentTime && (poll.endTime ?: Long.MAX_VALUE) > currentTime
    }
} 