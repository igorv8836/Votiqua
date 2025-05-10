package org.example.votiqua.server.feature.voting.domain.usecase

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.HTTPUnauthorizedException
import org.example.votiqua.server.feature.voting.data.repository.PollRepository
import org.example.votiqua.server.feature.voting.data.repository.SortingType
import org.jetbrains.exposed.sql.SortOrder

class GetPollUseCase(
    private val pollRepository: PollRepository,
    private val favoritePollUseCase: Lazy<FavoritePollUseCase>,
) {
    suspend fun get(
        pollId: Int,
        userId: Int? = null,
    ) : Poll {
        val poll = getPollOrException(pollId)

        return if (userId != null) {
            fillContext(poll, userId)
        } else {
            poll
        }.takeIfHasAccess(userId) ?: throw HTTPUnauthorizedException("Poll is private")
    }

    suspend fun getUserPolls(userId: Int, limit: Int = 10, offset: Int = 0): List<Poll> {
        return pollRepository.getUserPolls(userId, limit, offset).mapNotNull {
            fillContext(it, userId).takeIfHasAccess(userId)
        }
    }

    suspend fun getParticipatedPolls(userId: Int, limit: Int = 10, offset: Int = 0): List<Poll> {
        return pollRepository.getParticipatedPolls(userId, limit, offset).mapNotNull {
            fillContext(it, userId).takeIfHasAccess(userId)
        }
    }

    suspend fun getPollOrException(pollId: Int): Poll {
        return pollRepository.getPollById(pollId)
            ?: throw HTTPConflictException("Poll not found")
    }

    suspend fun searchPolls(userId: Int, query: String, limit: Int, offset: Int = 0): List<Poll> {
        return pollRepository.searchPolls(query, limit, offset).mapNotNull {
            fillContext(it, userId).takeIfHasAccess(userId)
        }
    }
    
    suspend fun findByLink(link: String, userId: Int? = null): Poll? {
        val poll = pollRepository.findByLink(link) ?: return null
        
        return if (userId != null) {
            fillContext(poll, userId)
        } else {
            poll
        }
    }
    
    suspend fun getPolls(
        limit: Int,
        offset: Int = 0,
        sortField: SortingType = SortingType.CreateDate,
        sortOrder: SortOrder = SortOrder.DESC,
        needIsOpen: Boolean = true,
        userId: Int
    ): List<Poll> {
        val polls = pollRepository.getPolls(limit, offset, sortField, sortOrder, needIsOpen)
        
        return polls.mapNotNull { fillContext(it, userId).takeIfHasAccess(userId) }
    }
    
    suspend fun getPopularPolls(
        limit: Int,
        offset: Int = 0,
        needIsOpen: Boolean = true,
        userId: Int
    ): List<Poll> {
        val polls = pollRepository.getPopularPolls(limit, offset, needIsOpen)
        
        return polls.mapNotNull { fillContext(it, userId).takeIfHasAccess(userId) }
    }
    
    suspend fun searchPollTitles(query: String, limit: Int): List<String> {
        return pollRepository.searchPollTitles(query, limit)
    }

    fun Poll.takeIfHasAccess(userId: Int?): Poll? {
        val hasAccess = this.isOpen ||
                this.authorId == userId ||
                this.members.firstOrNull { it.user.id == userId } != null

        return this.takeIf { hasAccess }
    }

    suspend fun fillContext(poll: Poll, userId: Int): Poll {
        val isFavorite = favoritePollUseCase.value.isFavorite(userId, poll.id)
        return poll.copy(
            context = poll.context.copy(
                isAdmin = userId == poll.authorId,
                selectedOption = poll.members.firstOrNull { it.user.id == userId }?.optionId,
                totalVotes = poll.members.count { it.voted },
                memberCount = poll.members.size,
                isFavorite = isFavorite,
                isMember = poll.members.firstOrNull { it.user.id == userId } != null,
            ),
        )
    }
}