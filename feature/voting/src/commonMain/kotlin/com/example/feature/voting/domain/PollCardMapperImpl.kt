package com.example.feature.voting.domain

import com.example.feature.voting.domain.models.PollCardState
import com.example.feature.voting.utils.formatDate
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.votiqua.models.poll.Poll

class PollCardMapperImpl(
    private val pollStatusMapper: PollStatusMapper,
) : PollCardMapper {
    override fun mapToState(poll: Poll): PollCardState {
        val category = if (poll.tags.isNotEmpty()) {
            val tagNames = poll.tags.take(3).joinToString(", ") { it.name }
            if (poll.tags.size > 3) "$tagNames..." else tagNames
        } else {
            "Без категории"
        }

        val endDate = poll.endTime?.let {
            "До " + formatDate(Instant.fromEpochSeconds(it).toLocalDateTime(TimeZone.currentSystemDefault()))
        } ?: "Бессрочный"

        val creationDate = formatDate(
            Instant.fromEpochSeconds(poll.createdAt).toLocalDateTime(TimeZone.currentSystemDefault())
        )

        val statusText = pollStatusMapper.map(
            isStarted = poll.isStarted,
            startTime = poll.startTime,
            endTime = poll.endTime,
        )

        return PollCardState(
            id = poll.id,
            title = poll.question,
            endDate = endDate,
            participants = poll.members.size,
            status = statusText,
            description = poll.description ?: "",
            category = category,
            creationDate = creationDate,
            isFavorite = poll.context.isFavorite,
        )
    }

    override fun mapToState(polls: List<Poll>): List<PollCardState> {
        return polls.map {
            mapToState(it)
        }
    }
}