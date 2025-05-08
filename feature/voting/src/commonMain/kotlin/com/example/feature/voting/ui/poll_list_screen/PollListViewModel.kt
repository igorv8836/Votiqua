@file:OptIn(OrbitExperimental::class)

package com.example.feature.voting.ui.poll_list_screen

import androidx.lifecycle.ViewModel
import com.example.common.SnackbarManager
import com.example.feature.voting.data.repository.PollRepository
import com.example.feature.voting.domain.models.UiPoll
import com.example.feature.voting.utils.formatDate
import com.example.orbit_mvi.viewmodel.container
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.votiqua.models.poll.Poll
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental

class PollListViewModel(
    private val pollRepository: PollRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel(), ContainerHost<PollListState, PollListEffect> {
    override val container: Container<PollListState, PollListEffect> = container(PollListState())

    init {
        loadPolls()
    }

    fun loadPolls() = intent {
        reduce {
            state.copy(
                myPollsIsLoading = true,
                otherPollsIsLoading = true,
            )
        }

        loadMyPolls()
        loadOtherPolls()
    }

    private suspend fun loadMyPolls() = subIntent {
        val result = pollRepository.getMyPolls()

        result.onSuccess {
            reduce {
                state.copy(
                    myPolls = it.map { it.toUiPoll() },
                    myPollsIsLoading = false,
                )
            }
        }.onFailure {
            snackbarManager.sendMessage(it.message)
        }
    }

    private suspend fun loadOtherPolls() = subIntent {
        val result = pollRepository.getOtherPolls()

        result.onSuccess {
            reduce {
                state.copy(
                    otherPolls = it.map { it.toUiPoll() },
                    otherPollsIsLoading = false,
                )
            }
        }.onFailure {
            snackbarManager.sendMessage(it.message)
        }
    }
}

private fun Poll.toUiPoll(): UiPoll {
    val category = if (tags.isNotEmpty()) {
        val tagNames = tags.take(3).joinToString(", ") { it.name }
        if (tags.size > 3) "$tagNames..." else tagNames
    } else {
        "Без категории"
    }

    return UiPoll(
        id = this.id,
        title = question,
        endDate = (this.endTime?.let {
            "До " + formatDate(Instant.fromEpochSeconds(it).toLocalDateTime(TimeZone.currentSystemDefault()))
        }) ?: "Бессрочный",
        participants = members.size,
        status = "Открыто", // TODO
        description = this.description ?: "",
        category = category,
        creationDate = formatDate(Instant.fromEpochSeconds(this.createdAt)
            .toLocalDateTime(TimeZone.currentSystemDefault())),
    )
}