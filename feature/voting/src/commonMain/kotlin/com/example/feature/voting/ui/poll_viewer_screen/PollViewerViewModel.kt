package com.example.feature.voting.ui.poll_viewer_screen

import androidx.lifecycle.ViewModel
import com.example.feature.voting.data.repository.PollRepository
import com.example.feature.voting.domain.models.Participant
import com.example.orbit_mvi.viewmodel.container
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

internal class PollViewerViewModel(
    private val pollRepository: PollRepository,
) : ContainerHost<PollViewerState, PollViewerSideEffect>, ViewModel() {

    override val container: Container<PollViewerState, PollViewerSideEffect> =
        container(PollViewerState()) {
            loadPoll(1)
        }

    private fun loadPoll(pollId: Int) = intent {
        val res = pollRepository.getPoll(pollId)

        res.onSuccess { poll ->
            val isAdmin = true // TODO
            reduce {
                state.copy(
                    pollId = poll.id,
                    title = poll.question,
                    description = poll.description.orEmpty(),
                    options = poll.options.map { it.optionText },
                    participants = generateDummyParticipants(),
                    anonymous = false,
                    votesExist = poll.options.any { it.voteCount > 0 },
                    isAdmin = isAdmin
                )
            }
        }.onFailure {
            TODO()
        }
    }

    private fun generateDummyParticipants(): List<Participant> {
        return (0..10).map { index ->
            Participant(
                id = index,
                name = "User $index",
                avatarUrl = "https://avatars.mds.yandex.net/i?id=9785f59e5d941e55882930681a09a53932226e63-11376477-images-thumbs&n=13",
                voted = index % 2 == 0,
                selectedOption = index.toString()
            )
        }
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
    val votesExist: Boolean = false,
    val isAdmin: Boolean = false
)

sealed interface PollViewerSideEffect {
    object EditRequested : PollViewerSideEffect
}
