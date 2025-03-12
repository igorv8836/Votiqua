package org.example.votiqua.ui.manage_poll_screen

import androidx.lifecycle.ViewModel
import com.example.orbit_mvi.viewmodel.container
import org.example.votiqua.domain.model.Participant
import org.example.votiqua.domain.model.poll.Poll
import org.example.votiqua.domain.model.poll.PollOption
import org.example.votiqua.domain.repository.PollRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

internal class ManagePollViewModel(
    private val pollRepository: PollRepository,
) : ContainerHost<ManagePollState, ManagePollSideEffect>, ViewModel() {

    override val container: Container<ManagePollState, ManagePollSideEffect> = container(ManagePollState()) {
        val participants = (0..30).map {
            Participant(
                it,
                "Person $it",
//                null,
                "https://avatars.mds.yandex.net/i?id=9785f59e5d941e55882930681a09a53932226e63-11376477-images-thumbs&n=13",
                it % 3 == 0,
                it.toString()
            )
        }

        intent {
            reduce {
                state.copy(saved = false, participants = participants)
            }
        }
    }

    init {
        loadPoll(1)
    }

    private fun loadPoll(pollId: Int) = intent {
        val poll = pollRepository.getPollById(pollId) ?: return@intent
        reduce {
            state.copy(
                title = poll.title,
                description = poll.description.orEmpty(),
                options = poll.options.map { it.text },
                votesExist = poll.options.any { it.votes > 0 }
            )
        }
    }

    fun onTitleChanged(newTitle: String) = blockingIntent {
        reduce { state.copy(title = newTitle) }
    }

    fun onDescriptionChanged(newDescription: String) = blockingIntent {
        reduce { state.copy(description = newDescription) }
    }

    fun onOptionChanged(index: Int, newOption: String) = intent {
        val updatedOptions = state.options.toMutableList().also { it[index] = newOption }
        reduce { state.copy(options = updatedOptions) }
    }

    fun addOption() = intent {
        reduce { state.copy(options = state.options + "") }
    }

    fun removeOption(index: Int) = intent {
        reduce { state.copy(options = state.options.filterIndexed { i, _ -> i != index }) }
    }

    fun saveChanges() = intent {
        reduce { state.copy(isSaving = true, error = null) }
        try {
            pollRepository.updatePoll(state.toPoll())
            postSideEffect(ManagePollSideEffect.Saved)
        } catch (e: Exception) {
            reduce { state.copy(error = e.message, isSaving = false) }
        }
    }

    fun requestDelete() = intent {
        deletePoll()
    }

    private fun deletePoll() = intent {
        reduce { state.copy(isDeleting = true) }
        try {
            pollRepository.deletePoll(state.pollId)
            postSideEffect(ManagePollSideEffect.Deleted)
        } catch (e: Exception) {
            reduce { state.copy(error = e.message, isDeleting = false) }
        }
    }
}

data class ManagePollState(
    val pollId: Int = -1,
    val title: String = "",
    val anonymous: Boolean = false,
    val participants: List<Participant> = emptyList(),
    val isOpen: Boolean = false,
    val link: String = "link",
    val description: String = "",
    val options: List<String> = emptyList(),
    val votesExist: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)

sealed interface ManagePollSideEffect {
    data object Saved : ManagePollSideEffect
    data object Deleted : ManagePollSideEffect
}


fun ManagePollState.toPoll(): Poll {
    return Poll(
        id = pollId,
        title = title,
        description = description.takeIf { it.isNotBlank() },
        options = options.map {
            PollOption(
                text = it,
                id = 1,
                votes = 0
            )
        }
    )
}
