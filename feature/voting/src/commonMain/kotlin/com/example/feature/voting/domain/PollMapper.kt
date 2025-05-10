package com.example.feature.voting.domain

import com.example.feature.voting.domain.models.Participant
import com.example.feature.voting.ui.manage_poll_screen.ManagePollState
import com.example.feature.voting.ui.poll_viewer_screen.OptionAndCounts
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

class PollMapper(
    private val pollStatusMapper: PollStatusMapper,
) {

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
            authorId = 0,
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
            votesExist = poll.context.totalVotes != 0,
            isSaving = false,
            isDeleting = false,
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
                    id = it.user.id,
                    name = it.user.username,
                    avatarUrl = it.user.photoUrl,
                    voted = it.voted,
                    selectedOption = it.optionText,
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
            .mapIndexed { index, item ->
                OptionAndCounts(
                    id = item.id,
                    index = index,
                    option = item.optionText,
                    count = item.voteCount,
                )
            }

        val participants = poll.members.map {
            Participant(
                id = it.user.id,
                name = it.user.username,
                avatarUrl = it.user.photoUrl,
                voted = it.voted,
                selectedOption = it.optionText,
            )
        }

        val startTime = poll.startTime
        val endTime = poll.endTime

        val statusText = pollStatusMapper.map(
            isStarted = poll.isStarted,
            startTime = poll.startTime,
            endTime = poll.endTime,
        )

        val votingAvailable = statusText == "Активно"

        val votingPeriod = if (startTime != null && endTime != null) {
            val zone = TimeZone.currentSystemDefault()
            val startDt = Instant.fromEpochSeconds(startTime).toLocalDateTime(zone)
            val endDt = Instant.fromEpochSeconds(endTime).toLocalDateTime(zone)
            "${formatDate(startDt)} ${formatTime(startDt)} - ${formatDate(endDt)} ${formatTime(endDt)}"
        } else {
            null
        }

        return PollViewerState(
            pollId = poll.id,
            title = poll.question,
            description = poll.description ?: "",
            options = options,
            statusText = statusText,
            participants = participants,
            anonymous = poll.isAnonymous,
            isAdmin = poll.context.isAdmin,
            voteCount = poll.context.totalVotes,
            memberCount = poll.context.memberCount,
            votingAvailable = votingAvailable,
            selectedOption = poll.context.selectedOption,
            votingPeriod = votingPeriod,
            isMember = poll.context.isMember,
        )
    }
}