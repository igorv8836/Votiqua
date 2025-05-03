package com.example.feature.voting.ui.manage_poll_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.feature.voting.data.repository.PollRepository
import com.example.feature.voting.domain.models.Participant
import com.example.feature.voting.utils.toPoll
import com.example.orbit_mvi.viewmodel.container
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.votiqua.models.common.ErrorType
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
                state.copy(participants = participants)
            }
        }
    }

    init {
        loadPoll(1)
    }

    private fun loadPoll(pollId: Int) = intent {
        val result = pollRepository.getPoll(pollId)
        result.onSuccess { poll ->
            reduce {
                state.copy(
                    title = poll.question,
                    description = poll.description.orEmpty(),
                    options = poll.options.map { it.optionText },
                    votesExist = poll.options.any { it.voteCount > 0 }
                )
            }
        }.onFailure {
            //  TODO
        }
    }

    fun saveChanges() = intent {
        reduce { state.copy(isSaving = true) }

        val newPoll = state
        val errorText = if (newPoll.title.isBlank()) {
            "Вопрос не может быть пустым"
        } else if (newPoll.description.isBlank()) {
            "Описание не может быть пустым"
        } else if (newPoll.options.size < 2) {
            "Количество вариантов должно быть не меньше 2"
        } else if (newPoll.startDateLong != null || newPoll.startTimeLong != null || newPoll.endTimeLong != null || newPoll.endDateLong != null) {
            if (newPoll.startDateLong != null && newPoll.startTimeLong != null && newPoll.endTimeLong != null && newPoll.endDateLong != null) {
                if (newPoll.startDateLong + newPoll.startTimeLong >= newPoll.endDateLong + newPoll.endTimeLong) {
                    "Время завершения не может быть меньше, чем время старта"
                } else {
                    null
                }
            } else {
                "Заполните полностью дату и время, либо оставьте пустым"
            }
        } else {
            null
        }

        errorText?.let {
            postSideEffect(ManagePollSideEffect.ErrorMessage(it))
            reduce { state.copy(isSaving = false) }
            return@intent
        }

        val poll = state.toPoll()
        val result = if (state.pollId <= 0) {
            pollRepository.createPoll(poll)
        } else {
            pollRepository.updatePoll(poll)
        }

        result.onSuccess {
            postSideEffect(ManagePollSideEffect.Saved)
        }.onFailure {
            postSideEffect(ManagePollSideEffect.ErrorMessage(it.message ?: ErrorType.GENERAL.message))
        }
        reduce { state.copy(isSaving = false) }
    }

    fun startPoll() = intent {
        TODO()
    }

    fun deletePoll() = intent {
        reduce { state.copy(isDeleting = true) }
        try {
//            pollRepository.deletePoll(state.pollId) //  TODO
            postSideEffect(ManagePollSideEffect.Deleted)
        } catch (e: Exception) {
//            reduce { state.copy(error = e.message, isDeleting = false) }
        }
    }




    fun setPollId(id: Int) = blockingIntent {
        reduce { state.copy(pollId = id) }
    }

    fun onTitleChanged(newTitle: String) = blockingIntent {
        reduce { state.copy(title = newTitle) }
    }

    fun onDescriptionChanged(newDescription: String) = blockingIntent {
        reduce { state.copy(description = newDescription) }
    }

    fun onMultipleChoiceClicked() = blockingIntent {
        reduce { state.copy(multipleChoice = !state.multipleChoice) }
    }

    fun onStartTimeChanged(newStartTime: Long) = blockingIntent {
        val h = newStartTime / 1000 / 3600
        val m = newStartTime / 1000 / 60 % 60
        val formatted = formatTime(h.toInt(), m.toInt())
        reduce {
            state.copy(
                startTime = formatted,
                startTimeLong = newStartTime,
            )
        }
    }

    fun onStartDateChanged(newStartDate: Long) = blockingIntent {
        val picked = Instant.fromEpochMilliseconds(newStartDate)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val formatted = formatDate(picked.year, picked.monthNumber, picked.dayOfMonth)
        reduce {
            state.copy(
                startDate = formatted,
                startDateLong = newStartDate
            )
        }
    }

    fun onEndTimeChanged(newEndTime: Long) = blockingIntent {
        val h = newEndTime / 1000 / 3600
        val m = newEndTime / 1000 / 60 % 60
        val formatted = formatTime(h.toInt(), m.toInt())
        reduce {
            state.copy(
                endTime = formatted,
                endTimeLong = newEndTime,
            )
        }
    }

    fun onEndDateChanged(newEndDate: Long) = blockingIntent {
        val picked = Instant.fromEpochMilliseconds(newEndDate)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val formatted = formatDate(picked.year, picked.monthNumber, picked.dayOfMonth)
        reduce {
            state.copy(
                endDate = formatted,
                endDateLong = newEndDate,
            )
        }
    }

    fun onTagAdded(tag: String) = blockingIntent {
        reduce { state.copy(tags = state.tags + tag) }
    }

    fun onTagRemoved(tag: String) = blockingIntent {
        reduce { state.copy(tags = state.tags.filter { it != tag }) }
    }

    fun onAnonClicked() = blockingIntent {
        reduce { state.copy(anonymous = !state.anonymous) }
    }

    fun onOpenClicked() = blockingIntent {
        reduce { state.copy(isOpen = !state.isOpen) }
    }

    fun onOptionChanged(index: Int, newOption: String) = blockingIntent {
        val updatedOptions = state.options.toMutableList().also { it[index] = newOption }
        reduce { state.copy(options = updatedOptions) }
    }

    fun addOption() = intent {
        reduce { state.copy(options = state.options + "") }
    }

    fun removeOption(index: Int) = intent {
        reduce { state.copy(options = state.options.filterIndexed { i, _ -> i != index }) }
    }
}

@Stable
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

    val multipleChoice: Boolean = false,
    val startTime: String? = null,
    val startTimeLong: Long? = null,
    val startDate: String? = null,
    val startDateLong: Long? = null,
    val endTime: String? = null,
    val endTimeLong: Long? = null,
    val endDate: String? = null,
    val endDateLong: Long? = null,
    val tags: List<String> = emptyList()
)

sealed interface ManagePollSideEffect {
    data object Saved : ManagePollSideEffect
    data object Deleted : ManagePollSideEffect
    data class ErrorMessage(val message: String) : ManagePollSideEffect
}

private fun formatDate(year: Int, month: Int, day: Int): String = buildString {
    append(day.toString().padStart(2, '0'))
    append('-')
    append(month.toString().padStart(2, '0'))
    append('-')
    append(year.toString().padStart(4, '0'))
}

private fun formatTime(hour: Int, minute: Int): String = buildString {
    append(hour.toString().padStart(2, '0'))
    append(':')
    append(minute.toString().padStart(2, '0'))
}