package com.example.feature.voting.utils

import com.example.feature.voting.domain.models.Participant
import com.example.feature.voting.ui.manage_poll_screen.ManagePollState
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.models.poll.PollOption
import org.example.votiqua.models.poll.Tag

fun ManagePollState.toPoll(): Poll {
    return Poll(
        id = pollId,
        question = title,
        description = description,
        isMultiple = multipleChoice,
        isAnonymous = anonymous,
        isOpen = isOpen,
        createdAt = 0L,
        startTime = if (startTimeLong != null && startDateLong != null) startTimeLong + startDateLong else null,
        endTime = if (endTimeLong != null && endDateLong != null) endTimeLong + endDateLong else null,
        options = options.mapIndexed { index, item ->
            PollOption(
                pollId = pollId,
                optionText = item,
                orderIndex = index,
            )
        },
        tags = tags.map {
            Tag(
                name = it,
            )
        },
        totalVotes = 0,
        authorId = 0,
        isFavorite = false,
        members = emptyList(),
    )
}

fun Poll.toState(): ManagePollState {
    val lStartTime = startTime?.let { it % (24 * 3600) }
    val lStartDate = if (this.startTime != null && lStartTime != null) {
        this.startTime?.let {
            it - lStartTime
        }
    } else {
        null
    }

    val lEndTime = endTime?.let { it % (24 * 3600) }
    val lEndDate = if (this.endTime != null && lEndTime != null) {
        this.endTime?.let {
            it - lEndTime
        }
    } else {
        null
    }
    return ManagePollState(
        pollId = id,
        title = question,
        description = this.description ?: "",
        anonymous = isAnonymous,
        isOpen = isOpen,
        link = "link", // TODO
        votesExist = false, // TODO
        isSaving = false,
        isDeleting = false,
        multipleChoice = isMultiple,

        startTime = lStartTime?.let {
            formatTime(
                LocalDateTime(
                    date = LocalDate(1, 1, 1),
                    time = LocalTime((it / 3600).toInt(), ((it / 60) % 60).toInt()),
                )
            )
        } ?: "",
        startTimeLong = lStartTime,
        startDate = lStartDate?.let {
            formatDate(Instant.fromEpochSeconds(it).toLocalDateTime(TimeZone.currentSystemDefault()))
        } ?: "",
        startDateLong = lStartDate,

        endTime = lEndTime?.let {
            formatTime(
                LocalDateTime(
                    date = LocalDate(1, 1, 1),
                    time = LocalTime((it / 3600).toInt(), ((it / 60) % 60).toInt()),
                )
            )
        } ?: "",
        endTimeLong = lEndTime,
        endDate = lEndDate?.let {
            formatDate(Instant.fromEpochSeconds(it).toLocalDateTime(TimeZone.currentSystemDefault()))
        } ?: "",
        endDateLong = lEndDate,

        tags = this.tags.map { it.name },
        participants = this.members.map {
            Participant(
                id = it.id,
                name = it.username,
                avatarUrl = it.photoUrl,
                voted = false,
                selectedOption = null,
            )
        },
        options = this.options.map { it.optionText },
    )
}