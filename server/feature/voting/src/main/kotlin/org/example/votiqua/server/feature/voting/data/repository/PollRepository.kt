package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.common.utils.toUtcDateTime
import org.example.votiqua.server.feature.voting.database.PollParticipantTable
import org.example.votiqua.server.feature.voting.database.PollTable
import org.example.votiqua.server.feature.voting.database.VoteTable
import org.example.votiqua.server.feature.voting.utils.pollLink
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.util.UUID

class PollRepository(
    private val tagRepository: TagRepository,
    private val pollOptionRepository: PollOptionRepository,
    private val pollParticipantRepository: PollParticipantRepository,
) : BasePollRepository(tagRepository, pollOptionRepository, pollParticipantRepository) {
    suspend fun getPollById(id: Int): Poll? {
        return dbQuery {
            val pollRow = PollTable.select { PollTable.id eq id }.singleOrNull() ?: return@dbQuery null
            val poll = mapRowToPoll(pollRow)

            return@dbQuery fillPollInfo(poll)
        }
    }

    suspend fun createPoll(poll: Poll, authorId: Int): Poll {
        val pollId = dbQuery {
            val pollId = PollTable.insert {
                it[question] = poll.question
                it[description] = poll.description
                it[isAnonymous] = poll.isAnonymous
                it[isOpen] = poll.isOpen
                it[createdAt] = currentDateTime()
                it[startDate] = poll.startTime?.toUtcDateTime()
                it[endDate] = poll.endTime?.toUtcDateTime()
                it[PollTable.authorId] = authorId
                it[link] = generatePollLink()
            } get PollTable.id

            pollOptionRepository.insertOptions(
                pollId,
                poll.options
            )
            tagRepository.insertTags(poll.tags, pollId)

            pollParticipantRepository.insert(
                userId = authorId,
                pollId = pollId,
            )

            pollId
        }
        return getPollById(pollId) ?: throw HTTPConflictException("Error in creating")
    }

    suspend fun deletePoll(pollId: Int): Boolean {
        return dbQuery {
            val deleted = PollTable.deleteWhere {
                PollTable.id eq pollId
            }
            deleted > 0
        }
    }

    suspend fun updatePoll(poll: Poll): Poll? {
        dbQuery {
            PollTable.update({ PollTable.id eq poll.id }) {
                it[question] = poll.question
                it[description] = poll.description
                it[isAnonymous] = poll.isAnonymous
                it[isOpen] = poll.isOpen
                it[startDate] = poll.startTime?.toUtcDateTime()
                it[endDate] = poll.endTime?.toUtcDateTime()
            }

            pollOptionRepository.updateOptions(
                pollId = poll.id,
                options = poll.options,
            )
            tagRepository.updatePollTags(poll)
        }
        return getPollById(poll.id)
    }

    suspend fun regeneratePollLink(pollId: Int): String {
        val newLink = generatePollLink()
        dbQuery {
            PollTable.update({ PollTable.id eq pollId }) {
                it[link] = newLink
            }
        }
        return newLink
    }

    private fun generatePollLink(): String {
        return pollLink + UUID.randomUUID().toString().take(20)
    }

    suspend fun startPoll(pollId: Int) {
        dbQuery {
            PollTable.update({ PollTable.id eq pollId }) {
                it[isStarted] = true
            }
        }
    }

    suspend fun searchPolls(query: String, limit: Int, offset: Int = 0): List<Poll> {
        return dbQuery {
            val polls = PollTable
                .select { PollTable.question.like("%$query%") and (PollTable.isOpen eq true) }
                .orderBy(PollTable.createdAt, SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map { mapRowToPoll(it) }

            fillPollsWithOptionsAndTags(polls)
        }
    }

    suspend fun getUserPolls(userId: Int, limit: Int, offset: Int): List<Poll> {
        return dbQuery {
            val polls = PollTable
                .select { PollTable.authorId eq userId }
                .orderBy(PollTable.createdAt, SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map { mapRowToPoll(it) }

            fillPollsWithOptionsAndTags(polls)
        }
    }

    suspend fun getParticipatedPolls(userId: Int, limit: Int, offset: Int): List<Poll> {
        return dbQuery {
            val polls = PollTable
                .innerJoin(PollParticipantTable, { PollTable.id }, { PollParticipantTable.pollId })
                .select {
                    (PollParticipantTable.userId eq userId) and (PollTable.authorId neq userId)
                }
                .orderBy(PollTable.createdAt, SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map { mapRowToPoll(it) }

            fillPollsWithOptionsAndTags(polls)
        }
    }

    suspend fun searchPollTitles(query: String, limit: Int): List<String> {
        return dbQuery {
            PollTable
                .slice(PollTable.question)
                .select { PollTable.question.like("%$query%") and (PollTable.isOpen eq true) }
                .orderBy(PollTable.createdAt, SortOrder.DESC)
                .limit(limit)
                .map { it[PollTable.question] }
        }
    }

    suspend fun checkAccess(userId: Int, pollId: Int): Boolean {
        return dbQuery {
            val poll = PollTable.select { PollTable.id eq pollId }.singleOrNull()?.let {
                mapRowToPoll(it)
            }

            if (poll?.isOpen == true || poll?.authorId == userId) return@dbQuery true

            val member = PollParticipantTable
                .select { (PollParticipantTable.pollId eq pollId) and (PollParticipantTable.userId eq userId) }
                .firstOrNull()

            member != null
        }
    }

    suspend fun votePoll(pollId: Int, optionId: Int, userId: Int) {
        return dbQuery {
            VoteTable.insert {
                it[VoteTable.pollId] = pollId
                it[VoteTable.optionId] = optionId
                it[VoteTable.userId] = userId
                it[VoteTable.votedAt] = currentDateTime()
            }
        }
    }

    suspend fun findByLink(link: String): Poll? {
        return dbQuery {
            if (link.isBlank()) return@dbQuery null
            val pollRow = PollTable.select { PollTable.link eq link }.singleOrNull() ?: return@dbQuery null
            val poll = mapRowToPoll(pollRow)

            return@dbQuery fillPollInfo(poll)
        }
    }

    suspend fun getPolls(
        limit: Int,
        offset: Int = 0,
        sortField: SortingType = SortingType.CreateDate,
        sortOrder: SortOrder = SortOrder.DESC,
        needIsOpen: Boolean = true,
    ): List<Poll> {
        return dbQuery {
            val orderBy = when (sortField) {
                SortingType.CreateDate -> PollTable.createdAt
                SortingType.StartDate -> PollTable.startDate
                SortingType.EndDate -> PollTable.endDate
            }
            
            val polls = PollTable
                .select { (PollTable.isStarted eq true) and (PollTable.isOpen eq needIsOpen) }
                .orderBy(orderBy, sortOrder)
                .limit(limit, offset.toLong())
                .map { mapRowToPoll(it) }

            fillPollsWithOptionsAndTags(polls)
        }
    }

    suspend fun getPopularPolls(
        limit: Int,
        offset: Int = 0,
        needIsOpen: Boolean = true,
    ): List<Poll> {
        return dbQuery {
            val participantCount = PollParticipantTable.id.count().alias("pc")
            val expressions: List<Expression<*>> = PollTable.columns.map { it as Expression<*> } + participantCount

            val rows = PollTable
                .join(
                    PollParticipantTable,
                    JoinType.INNER,
                    additionalConstraint = { PollTable.id eq PollParticipantTable.pollId }
                )
                .slice(expressions)
                .select { (PollTable.isStarted eq true) and (PollTable.isOpen eq needIsOpen) }
                .groupBy(PollTable.id)
                .orderBy(participantCount, SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map { mapRowToPoll(it) }

            fillPollsWithOptionsAndTags(rows)
        }
    }

}

enum class SortingType {
    CreateDate,
    StartDate,
    EndDate,
}