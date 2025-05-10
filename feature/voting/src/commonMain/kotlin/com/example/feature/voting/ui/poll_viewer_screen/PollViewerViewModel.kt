package com.example.feature.voting.ui.poll_viewer_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.common.ResultExceptionHandler
import com.example.common.SnackbarManager
import com.example.common.handleException
import com.example.feature.voting.data.repository.PollRepository
import com.example.feature.voting.domain.PollMapper
import com.example.feature.voting.domain.models.Participant
import com.example.orbit_mvi.viewmodel.container
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

internal class PollViewerViewModel(
    pollId: Int,
    private val pollRepository: PollRepository,
    private val pollMapper: PollMapper,
    override val snackbarManager: SnackbarManager,
) : ContainerHost<PollViewerState, PollViewerSideEffect>, ViewModel(), ResultExceptionHandler {
    override val container: Container<PollViewerState, PollViewerSideEffect> = container(PollViewerState())

    init {
        loadPoll(pollId)
    }

    private fun loadPoll(pollId: Int) = intent {
        val res = pollRepository.getPoll(pollId)

        res.onSuccess { poll ->
            reduce {
                pollMapper.mapToPollViewerState(poll)
            }
        }.handleException(snackbarManager)
    }

    fun onEditClicked() = intent {
        postSideEffect(PollViewerSideEffect.EditRequested(state.pollId))
    }

    fun selectOption(optionId: Int) = intent {
        val res = pollRepository.vote(state.pollId, optionId)

        res.onSuccess {
            reduce {
                state.copy(
                    selectedOption = optionId,
                    options = state.options.map {
                        if (it.id == optionId) it.copy(count = it.count + 1) else it
                    },
                    voteCount = state.voteCount + 1,
                )
            }
        }.handleException()
    }

    fun setShowExitDialog(value: Boolean) = intent {
        reduce {
            state.copy(showExitDialog = value)
        }
    }

    fun confirmExiting() = intent {
        val res = pollRepository.leaveFromPoll(state.pollId)

        res.onSuccess {
            postSideEffect(PollViewerSideEffect.NavigateToLastScreen)
        }.handleException()
    }

    fun joinPoll() = intent {
        val res = pollRepository.joinByButton(state.pollId)

        res.onSuccess {
            loadPoll(state.pollId)
        }.handleException()
    }
}

@Stable
data class PollViewerState(
    val pollId: Int = -1,
    val title: String = "",
    val statusText: String = "",
    val description: String = "",
    val options: List<OptionAndCounts> = emptyList(),
    val participants: List<Participant> = emptyList(),
    val anonymous: Boolean = false,
    val isAdmin: Boolean = false,

    val selectedOption: Int? = null,
    val votingAvailable: Boolean = false,
    val voteCount: Int = 0,
    val memberCount: Int = 0,
    val votingPeriod: String? = null,

    val showExitDialog: Boolean = false,
    val isMember: Boolean = false,
)

data class OptionAndCounts(
    val id: Int,
    val index: Int,
    val option: String,
    val count: Int,
)

sealed interface PollViewerSideEffect {
    data class EditRequested(
        val pollId: Int,
    ) : PollViewerSideEffect

    data object NavigateToLastScreen : PollViewerSideEffect
}
