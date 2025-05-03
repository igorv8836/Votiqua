package com.example.feature.voting.utils

import com.example.feature.voting.ui.manage_poll_screen.ManagePollState
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