package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.common.utils.toLocalDateTimeMoscow
import org.example.votiqua.server.common.utils.toUtcDateTime
import org.example.votiqua.server.feature.voting.database.PollAuthorTable
import org.example.votiqua.server.feature.voting.database.PollOptionTable
import org.example.votiqua.server.feature.voting.database.PollParticipantTable
import org.example.votiqua.server.feature.voting.database.PollTable
import org.example.votiqua.server.feature.voting.database.PollTagTable
import org.example.votiqua.server.feature.voting.database.TagTable
import org.example.votiqua.server.feature.voting.database.VoteTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class PollRepository : BasePollRepository() {
    suspend fun getPollById(id: Int, userId: Int? = null): Poll? {
        return dbQuery {
            val pollRow = PollTable.select { PollTable.id eq id }.singleOrNull() ?: return@dbQuery null
            val poll = mapRowToPoll(pollRow)

            val options = PollOptionTable
                .select { PollOptionTable.pollId eq id }
                .orderBy(PollOptionTable.orderIndex)
                .map { mapRowToPollOption(it) }

            val tags = (PollTagTable innerJoin TagTable)
                .select { PollTagTable.pollId eq id }
                .map { mapRowToTag(it) }

            poll.copy(options = options, tags = tags)
        }
    }

    suspend fun createPoll(poll: Poll, authorId: Int): Poll {
        val pollId = dbQuery {
            val pollId = PollTable.insert {
                it[question] = poll.question
                it[description] = poll.description
                it[isMultiple] = poll.isMultiple
                it[isAnonymous] = poll.isAnonymous
                it[isOpen] = poll.isOpen
                it[createdAt] = currentDateTime()
                it[startDate] = poll.startDate?.toUtcDateTime()
                it[endDate] = poll.endDate?.toUtcDateTime()
            } get PollTable.id

            poll.options.forEach { option ->
                PollOptionTable.insert {
                    it[PollOptionTable.pollId] = pollId
                    it[optionText] = option.optionText
                    it[orderIndex] = option.orderIndex
                }
            }

            poll.tags.forEach { tag ->
                val tagId = TagTable.select { TagTable.name eq tag.name }
                    .singleOrNull()?.get(TagTable.id) ?: (TagTable.insert {
                    it[TagTable.name] = tag.name
                } get TagTable.id)

                PollTagTable.insert {
                    it[PollTagTable.pollId] = pollId
                    it[PollTagTable.tagId] = tagId
                }
            }

            PollParticipantTable.insert {
                it[PollParticipantTable.pollId] = pollId
                it[PollParticipantTable.userId] = authorId
                it[PollParticipantTable.createdAt] = currentDateTime()
            }

            pollId
        }
        return getPollById(pollId, authorId) ?: throw HTTPConflictException("Error in creating")
    }

    suspend fun votePoll(pollId: Int, optionId: Int, userId: Int): Boolean {
        return dbQuery {
            val poll = PollTable.select { PollTable.id eq pollId }.singleOrNull() ?: return@dbQuery false

            VoteTable.insert {
                it[VoteTable.pollId] = pollId
                it[VoteTable.optionId] = optionId
                it[VoteTable.userId] = userId
                it[VoteTable.votedAt] = currentDateTime()
            }

            true
        }
    }

    suspend fun deletePoll(pollId: Int, userId: Int): Boolean {
        return dbQuery {
            val deleted = PollTable.deleteWhere {
                PollTable.id eq pollId
            }
            deleted > 0
        }
    }

    suspend fun updatePoll(pollId: Int, poll: Poll): Poll? {
        val updated = dbQuery {
            PollTable.update({ PollTable.id eq pollId }) {
                it[question] = poll.question
                it[description] = poll.description
                it[isMultiple] = poll.isMultiple
                it[isAnonymous] = poll.isAnonymous
                it[isOpen] = poll.isOpen
                it[startDate] = poll.startDate?.toLocalDateTimeMoscow()
                it[endDate] = poll.endDate?.toLocalDateTimeMoscow()
            }
        }
        return if (updated > 0) getPollById(pollId) else null
    }

    suspend fun searchPolls(query: String, limit: Int): List<Poll> {
        return dbQuery {
            val polls = PollTable
                .select { PollTable.question.like("%$query%") }
                .orderBy(PollTable.createdAt, SortOrder.DESC)
                .limit(limit)
                .map { mapRowToPoll(it) }

            fillPollsWithOptionsAndTags(polls)
        }
    }

    suspend fun getUserPolls(userId: Int, limit: Int, offset: Int): List<Poll> {
        return dbQuery {
            val polls = (PollTable innerJoin PollAuthorTable)
                .select { PollAuthorTable.authorId eq userId }
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
                .select { PollTable.question.like("%$query%") }
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
}