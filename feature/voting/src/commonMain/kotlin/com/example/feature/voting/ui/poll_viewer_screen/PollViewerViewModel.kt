package com.example.feature.voting.ui.poll_viewer_screen

import androidx.lifecycle.ViewModel
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
    private val snackbarManager: SnackbarManager,
) : ContainerHost<PollViewerState, PollViewerSideEffect>, ViewModel() {

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
        postSideEffect(PollViewerSideEffect.EditRequested)
    }
}

data class PollViewerState(
    val pollId: Int = -1,
    val title: String = "",
    val description: String = "",
    val options: List<String> = emptyList(),
    val participants: List<Participant> = emptyList(),
    val anonymous: Boolean = false,
    val isAdmin: Boolean = false
)

sealed interface PollViewerSideEffect {
    object EditRequested : PollViewerSideEffect
}
