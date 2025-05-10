package org.example.votiqua.server.feature.voting.domain.usecase

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.HTTPUnauthorizedException
import org.example.votiqua.server.common.utils.dbQuery
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
        val poll = getPollUseCase.findByLink(link)
            ?: throw HTTPConflictException("Poll not found")

        dbQuery {
            pollParticipantRepository.insert(userId, poll.id)
        }

        return getPollUseCase.get(poll.id, userId)
    }

    suspend fun joinToPoll(pollId: Int, userId: Int) {
        val poll = getPollUseCase.get(pollId, userId)

        if (poll.isOpen) {
            dbQuery {
                pollParticipantRepository.insert(userId, poll.id)
            }
        } else {
            throw HTTPUnauthorizedException("Poll is private")
        }
    }

    private fun checkAccessForVoting(userId: Int, poll: Poll): Boolean {
        return if (poll.isOpen) {
            true
        } else {
            poll.members.any { it.user.id == userId }
        }
    }
} 