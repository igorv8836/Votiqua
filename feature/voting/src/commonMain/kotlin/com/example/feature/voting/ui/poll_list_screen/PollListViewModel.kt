@file:OptIn(OrbitExperimental::class)

package com.example.feature.voting.ui.poll_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.ResultExceptionHandler
import com.example.common.SnackbarManager
import com.example.feature.voting.data.repository.PollRepository
import com.example.feature.voting.domain.PollCardMapper
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental

class PollListViewModel(
    private val pollRepository: PollRepository,
    override val snackbarManager: SnackbarManager,
    private val pollCardMapper: PollCardMapper,
) : ViewModel(), ContainerHost<PollListState, PollListEffect>, ResultExceptionHandler {
    override val container: Container<PollListState, PollListEffect> = container(PollListState())

    init {
        loadPolls()

        viewModelScope.launch {
            pollRepository.myPolls.collectLatest {
                intent {
                    reduce {
                        state.copy(
                            myPolls = pollCardMapper.mapToState(it),
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            pollRepository.otherPolls.collectLatest {
                intent {
                    reduce {
                        state.copy(
                            otherPolls = pollCardMapper.mapToState(it),
                        )
                    }
                }
            }
        }
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

    fun onClickFavourite(pollId: Int) = intent {
        val res = pollRepository.toggleFavorite(pollId)

        res.onSuccess {
            val updatedMy = state.myPolls.map { card ->
                if (card.id == pollId) card.copy(isFavorite = !card.isFavorite) else card
            }
            val updatedOther = state.otherPolls.map { card ->
                if (card.id == pollId) card.copy(isFavorite = !card.isFavorite) else card
            }
            reduce {
                state.copy(
                    myPolls = updatedMy,
                    otherPolls = updatedOther
                )
            }
        }.handleException()
    }

    private suspend fun loadMyPolls() = subIntent {
        val result = pollRepository.getMyPolls()

        result.onSuccess {
            reduce {
                state.copy(
                    myPolls = pollCardMapper.mapToState(it),
                    myPollsIsLoading = false,
                )
            }
        }.handleException()
    }

    private suspend fun loadOtherPolls() = subIntent {
        val result = pollRepository.getOtherPolls()

        result.onSuccess {
            reduce {
                state.copy(
                    otherPolls = pollCardMapper.mapToState(it),
                    otherPollsIsLoading = false,
                )
            }
        }.handleException()
    }
}