package org.example.votiqua.presentation.polls.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.voting.data.repository.PollRepository
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

class FavoritePollsViewModel(
    private val pollRepository: PollRepository
) : ViewModel(), ContainerHost<FavoritePollsState, FavoritePollsEffect> {

    override val container: Container<FavoritePollsState, FavoritePollsEffect> = container(FavoritePollsState())

    // Загрузить избранные опросы
    fun loadFavorites() = intent {
        if (state.isLoading) return@intent

        reduce { state.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            pollRepository.getFavorites()
                .onSuccess { polls ->
                    reduce {
                        state.copy(
                            polls = polls,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    reduce {
                        state.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Не удалось загрузить избранные опросы"
                        )
                    }
                    postSideEffect(FavoritePollsEffect.ShowError(error.message ?: "Не удалось загрузить избранные опросы"))
                }
        }
    }

    // Перейти к деталям опроса
    fun navigateToPollDetails(pollId: Int) = intent {
        postSideEffect(FavoritePollsEffect.NavigateToDetails(pollId))
    }

    // Удалить из избранного
    fun removeFromFavorites(pollId: Int) = intent {
        viewModelScope.launch {
            pollRepository.toggleFavorite(pollId)
                .onSuccess {
                    // Обновляем список после удаления
                    loadFavorites()
                }
                .onFailure { error ->
                    postSideEffect(FavoritePollsEffect.ShowError(error.message ?: "Не удалось удалить из избранного"))
                }
        }
    }

    init {
        loadFavorites()
    }
} 