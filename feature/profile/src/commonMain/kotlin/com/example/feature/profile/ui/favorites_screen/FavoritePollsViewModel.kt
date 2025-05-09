package com.example.feature.profile.ui.favorites_screen

import androidx.lifecycle.ViewModel
import com.example.common.ResultExceptionHandler
import com.example.common.SnackbarManager
import com.example.feature.voting.data.repository.PollRepository
import com.example.feature.voting.domain.PollCardMapper
import com.example.orbit_mvi.viewmodel.container
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

internal class FavoritePollsViewModel(
    private val pollRepository: PollRepository,
    override val snackbarManager: SnackbarManager,
    private val pollCardMapper: PollCardMapper,
) : ViewModel(), ContainerHost<FavoritePollsState, FavoritePollsEffect>, ResultExceptionHandler {
    override val container: Container<FavoritePollsState, FavoritePollsEffect> = container(FavoritePollsState())

    init {
        loadFavorites()
    }

    fun loadFavorites() = intent {
        reduce { state.copy(isLoading = true) }
        val res = pollRepository.getFavorites()

        res.onSuccess { polls ->
            reduce {
                state.copy(
                    polls = pollCardMapper.mapToState(polls),
                    isLoading = false
                )
            }
        }.handleException(
            fallbackMessage = "Не удалось загрузить избранные опросы"
        ) { reduce { state.copy(isLoading = false) } }
    }

    fun removeFromFavorites(pollId: Int) = intent {
        val res = pollRepository.toggleFavorite(pollId)

        res.onSuccess {
            loadFavorites()
        }.handleException(fallbackMessage = "Не удалось удалить из избранного")
    }
} 