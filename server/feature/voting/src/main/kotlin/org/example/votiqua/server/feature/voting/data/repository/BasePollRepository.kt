package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.auth.SimpleUser
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.server.common.utils.toUtcTimestamp
import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.example.votiqua.server.feature.voting.database.PollParticipantTable
import org.example.votiqua.server.feature.voting.database.PollTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select

abstract class BasePollRepository(
    private val tagRepository: TagRepository,
    private val pollOptionRepository: PollOptionRepository,
) {
    protected fun mapRowToPoll(row: ResultRow): Poll {
        return Poll(
            id = row[PollTable.id],
            question = row[PollTable.question],
            description = row[PollTable.description],
            isMultiple = row[PollTable.isMultiple],
            isAnonymous = row[PollTable.isAnonymous],
            isOpen = row[PollTable.isOpen],
            createdAt = row[PollTable.createdAt].toUtcTimestamp(),
            startTime = row[PollTable.startDate]?.toUtcTimestamp(),
            endTime = row[PollTable.endDate]?.toUtcTimestamp()
        )
    }

    protected fun fillPollsWithOptionsAndTags(polls: List<Poll>): List<Poll> {
        if (polls.isEmpty()) return emptyList()

        return polls.map { poll ->
            val tags = tagRepository.getPollTags(poll.id)
            val options = pollOptionRepository.getOptions(poll.id)
            val members = getPollParticipants(poll.id)

            poll.copy(
                options = options,
                tags = tags,
                members = members,
            )
        }
    }

    private fun getPollParticipants(pollId: Int): List<SimpleUser> {
        return PollParticipantTable
            .innerJoin(UserTable, { PollParticipantTable.userId }, { UserTable.id })
            .select { PollParticipantTable.pollId eq pollId }
            .map {
                SimpleUser(
                    id = it[UserTable.id],
                    username = it[UserTable.username],
                    photoUrl = it[UserTable.photoUrl]
                )
            }
    }

}