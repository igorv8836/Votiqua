package com.example.feature.auth.ui.search_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.common.ResultExceptionHandler
import com.example.common.SnackbarManager
import com.example.feature.auth.data.repository.RecomRepository
import com.example.feature.auth.domain.QueryRecommendModel
import com.example.feature.auth.ui.search_screen.SearchState.Error
import com.example.feature.auth.ui.search_screen.SearchState.Loading
import com.example.feature.auth.ui.search_screen.SearchState.Success
import com.example.feature.voting.data.repository.PollRepository
import com.example.feature.voting.domain.PollCardMapper
import com.example.feature.voting.domain.models.PollCardState
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental

@Stable
sealed class SearchState {
    open val query: String = ""

    data class Loading(
        override val query: String,
    ) : SearchState()

    data class Success(
        override val query: String,
        val results: List<PollCardState> = emptyList(),
        val searchRecommends: List<QueryRecommendModel> = emptyList()
    ) : SearchState()

    data class Error(
        override val query: String,
        val message: String,
    ) : SearchState()
}

sealed interface SearchEvent {
    data class UpdateQuery(val query: String) : SearchEvent
    data class UpdateSearchText(val text: String) : SearchEvent
    data class DeleteQuery(val query: String) : SearchEvent
    data object Retry : SearchEvent
}

sealed class SearchEffect {
    data object AutoQuerySendEffect : SearchEffect()
}

@OptIn(OrbitExperimental::class)
class SearchViewModel(
    private val recomRepository: RecomRepository,
    private val pollRepository: PollRepository,
    override val snackbarManager: SnackbarManager,
    private val pollCardMapper: PollCardMapper,
) : ViewModel(), ContainerHost<SearchState, SearchEffect>, ResultExceptionHandler {

    private var autoSearchJob: Job? = null

    override val container = container<SearchState, SearchEffect>(
        initialState = Success(query = "", results = emptyList(), searchRecommends = emptyList())
    ) {
        loadInitialHistory()
    }

    private suspend fun loadInitialHistory() = subIntent {
        val historyQueries = recomRepository.getHistoryQueries()
        reduce {
            Success(
                query = state.query,
                results = (state as? Success)?.results ?: emptyList(),
                searchRecommends = historyQueries
            )
        }
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.UpdateQuery -> intent {
                handleSearchQuery(event.query)
            }
            is SearchEvent.UpdateSearchText -> {
                intent {
                    handleSearchTextUpdate(event.text)
                }
            }
            is SearchEvent.DeleteQuery -> intent {
                handleDeleteQuery(event.query)
            }
            SearchEvent.Retry -> intent {
                retryLastSearch()
            }
        }
    }

    private suspend fun handleSearchQuery(query: String) = subIntent {
        if (query.isEmpty()) {
            reduce { Success(query = query, results = emptyList(), searchRecommends = emptyList()) }
            return@subIntent
        }
        reduce { Loading(query = query) }

        val res = recomRepository.getPolls(query)

        res.onSuccess {
            reduce {
                Success(
                    query = query,
                    results = pollCardMapper.mapToState(it.results),
                    searchRecommends = emptyList(),
                )
            }
        }.onFailure {
            reduce {
                Error(
                    query = query,
                    message = it.message ?: "Ошибка при поиске"
                )
            }
        }
    }

    private suspend fun handleSearchTextUpdate(text: String) = subIntent {
        if (text.isEmpty()) {
            loadInitialHistory()
            return@subIntent
        }

        try {
            val recommends = recomRepository.getQueryRecommends(text)
            reduce {
                Success(
                    query = text,
                    results = (state as? Success)?.results ?: emptyList(),
                    searchRecommends = recommends
                )
            }
        } catch (e: Exception) {
            reduce { Error(query = text, message = "Ошибка рекомендаций: ${e.message}") }
        }
    }

    private suspend fun handleDeleteQuery(query: String) = subIntent {
        recomRepository.deleteQueryFromHistory(query)

        try {
            val recommends = recomRepository.getQueryRecommends(
                query = state.query,
            )
            reduce {
                Success(
                    query = state.query,
                    results = (state as? Success)?.results ?: emptyList(),
                    searchRecommends = recommends
                )
            }
        } catch (e: Exception) {
            reduce { Error(query = state.query, message = "Ошибка рекомендаций: ${e.message}") }
        }
    }

    private suspend fun retryLastSearch() = subIntent {
        when (val currentState = state) {
            is Error -> onEvent(SearchEvent.UpdateQuery(currentState.query))
            else -> Unit
        }
    }

    fun onClickFavourite(pollId: Int) = intent {
        val res = pollRepository.toggleFavorite(pollId)

        res.onSuccess {
            val state = state as? Success ?: return@onSuccess
            val results = state.results.map { card ->
                if (card.id == pollId) card.copy(isFavorite = !card.isFavorite) else card
            }
            reduce {
                state.copy(results = results)
            }
        }.handleException()
    }
}