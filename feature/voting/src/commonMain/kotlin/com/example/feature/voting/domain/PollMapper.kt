package com.example.feature.voting.domain

import com.example.feature.voting.domain.models.Participant
import com.example.feature.voting.ui.manage_poll_screen.ManagePollState
import com.example.feature.voting.ui.poll_viewer_screen.PollViewerState
import com.example.feature.voting.utils.formatDate
import com.example.feature.voting.utils.formatTime
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.votiqua.models.poll.Poll
import org.example.votiqua.models.poll.PollOption
import org.example.votiqua.models.poll.Tag

class PollMapper {

    fun mapToPoll(
        pollState: ManagePollState,
    ): Poll {
        val startTime = if (pollState.startTimeLong != null && pollState.startDateLong != null) {
            (pollState.startTimeLong + pollState.startDateLong) / 1000
        } else {
            null
        }

        val endTime = if (pollState.endTimeLong != null && pollState.endDateLong != null) {
            (pollState.endTimeLong + pollState.endDateLong) / 1000
        } else {
            null
        }

        return Poll(
            id = pollState.pollId,
            question = pollState.title,
            description = pollState.description,
            isMultiple = pollState.multipleChoice,
            isAnonymous = pollState.anonymous,
            isOpen = pollState.isOpen,
            createdAt = 0L,
            startTime = startTime,
            endTime = endTime,
            options = pollState.options.mapIndexed { index, item ->
                PollOption(
                    pollId = pollState.pollId,
                    optionText = item,
                    orderIndex = index,
                )
            },
            tags = pollState.tags.map {
                Tag(
                    name = it,
                )
            },
            totalVotes = 0,
            authorId = 0,
            isFavorite = false,
            members = emptyList(),
            link = pollState.link,
            isStarted = pollState.isStarted,
        )
    }

    fun mapToPollState(
        poll: Poll
    ): ManagePollState {
        val startTime = poll.startTime?.let { it * 1000 }
        val lStartTime = startTime?.let { it % (24 * 3600) }
        val lStartDate = if (startTime != null && lStartTime != null) {
            startTime - lStartTime
        } else {
            null
        }

        val endTime = poll.endTime?.let { it * 1000 }
        val lEndTime = endTime?.let { it % (24 * 3600) }
        val lEndDate = if (endTime != null && lEndTime != null) {
            endTime - lEndTime
        } else {
            null
        }
        return ManagePollState(
            pollId = poll.id,
            title = poll.question,
            description = poll.description ?: "",
            anonymous = poll.isAnonymous,
            isOpen = poll.isOpen,
            link = poll.link,
            votesExist = false, // TODO
            isSaving = false,
            isDeleting = false,
            multipleChoice = poll.isMultiple,
            isStarted = poll.isStarted,

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
                formatDate(Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()))
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
                formatDate(Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()))
            } ?: "",
            endDateLong = lEndDate,

            tags = poll.tags.map { it.name },
            participants = poll.members.map {
                Participant(
                    id = it.id,
                    name = it.username,
                    avatarUrl = it.photoUrl,
                    voted = false,
                    selectedOption = null,
                )
            },
            options = poll.options.map { it.optionText },
        )
    }

    fun mapToPollViewerState(
        poll: Poll,
    ): PollViewerState {
        val options = poll.options
            .sortedBy { it.orderIndex }
            .map { it.optionText }

        val participants = poll.members.map {
            Participant(
                id = it.id,
                name = it.username,
                avatarUrl = it.photoUrl,
                voted = false,
                selectedOption = null,
            )
        }

        return PollViewerState(
            pollId = poll.id,
            title = poll.question,
            description = poll.description ?: "",
            options = options,
            participants = participants,
            anonymous = poll.isAnonymous,
            isAdmin = false,
        )
    }
}