package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.models.poll.PollOption
import org.example.votiqua.models.poll.Tag
import org.example.votiqua.server.common.utils.toUtcTimestamp
import org.example.votiqua.server.feature.voting.database.PollOptionTable
import org.example.votiqua.server.feature.voting.database.PollTable
import org.example.votiqua.server.feature.voting.database.PollTagTable
import org.example.votiqua.server.feature.voting.database.TagTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

abstract class BasePollRepository {
    protected fun mapRowToPoll(row: ResultRow): Poll {
        return Poll(
            id = row[PollTable.id],
            question = row[PollTable.question],
            description = row[PollTable.description],
            isMultiple = row[PollTable.isMultiple],
            isAnonymous = row[PollTable.isAnonymous],
            isOpen = row[PollTable.isOpen],
            createdAt = row[PollTable.createdAt].toUtcTimestamp(),
            startDate = row[PollTable.startDate]?.toUtcTimestamp(),
            endDate = row[PollTable.endDate]?.toUtcTimestamp()
        )
    }

    protected fun mapRowToPollOption(row: ResultRow): PollOption {
        return PollOption(
            id = row[PollOptionTable.id],
            pollId = row[PollOptionTable.pollId],
            optionText = row[PollOptionTable.optionText],
            orderIndex = row[PollOptionTable.orderIndex]
        )
    }

    protected fun mapRowToTag(row: ResultRow): Tag {
        return Tag(
            id = row[TagTable.id],
            name = row[TagTable.name]
        )
    }

    protected fun fillPollsWithOptionsAndTags(polls: List<Poll>): List<Poll> {
        if (polls.isEmpty()) return emptyList()

        val pollIds = polls.map { it.id }

        val options = PollOptionTable
            .select { PollOptionTable.pollId inList pollIds }
            .orderBy(PollOptionTable.orderIndex)
            .map { mapRowToPollOption(it) }
            .groupBy { it.pollId }

        val tags = (PollTagTable innerJoin TagTable)
            .select { PollTagTable.pollId inList pollIds }
            .map { mapRowToTag(it) }
            .groupBy { it.id }

        return polls.map { poll ->
            poll.copy(
                options = options[poll.id] ?: emptyList(),
                tags = tags[poll.id] ?: emptyList()
            )
        }
    }
}