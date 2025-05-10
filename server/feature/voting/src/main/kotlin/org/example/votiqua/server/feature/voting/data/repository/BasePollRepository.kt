package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.utils.toUtcTimestamp
import org.example.votiqua.server.feature.voting.database.PollTable
import org.jetbrains.exposed.sql.ResultRow

abstract class BasePollRepository(
    private val tagRepository: TagRepository,
    private val pollOptionRepository: PollOptionRepository,
    private val pollParticipantRepository: PollParticipantRepository,
) {
    protected fun mapRowToPoll(row: ResultRow): Poll {
        return Poll(
            id = row[PollTable.id],
            question = row[PollTable.question],
            description = row[PollTable.description],
            isAnonymous = row[PollTable.isAnonymous],
            isOpen = row[PollTable.isOpen],
            authorId = row[PollTable.authorId],
            createdAt = row[PollTable.createdAt].toUtcTimestamp(),
            startTime = row[PollTable.startDate]?.toUtcTimestamp(),
            endTime = row[PollTable.endDate]?.toUtcTimestamp(),
            isStarted = row[PollTable.isStarted],
            link = row[PollTable.link],
        )
    }

    protected fun fillPollInfo(poll: Poll): Poll {
        val tags = tagRepository.getPollTags(poll.id)
        val options = pollOptionRepository.getOptions(poll.id)
        val members = pollParticipantRepository.getPollParticipants(poll.id)

        return poll.copy(
            options = options,
            tags = tags,
            members = members,
        )
    }

    protected fun fillPollsWithOptionsAndTags(polls: List<Poll>): List<Poll> {
        if (polls.isEmpty()) return emptyList()

        return polls.map { poll ->
            fillPollInfo(poll)
        }
    }
}