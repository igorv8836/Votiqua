package org.example.votiqua.ui.search_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.voting.domain.models.PollCardState
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.votiqua.data.repository.SearchRecommendRepository
import org.example.votiqua.domain.model.search.QueryRecommendModel
import org.example.votiqua.ui.main_screen.mockPolls
import org.example.votiqua.ui.search_screen.SearchState.Error
import org.example.votiqua.ui.search_screen.SearchState.Loading
import org.example.votiqua.ui.search_screen.SearchState.Success
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
    private val searchRecommendRepository: SearchRecommendRepository,
) : ViewModel(), ContainerHost<SearchState, SearchEffect> {

    private var autoSearchJob: Job? = null

    override val container = container<SearchState, SearchEffect>(
        initialState = Success(query = "", results = emptyList(), searchRecommends = emptyList())
    ) {
        loadInitialHistory()
    }

    private suspend fun loadInitialHistory() = subIntent {
        val historyQueries = searchRecommendRepository.getHistoryQueries()
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
        delay(2000)
        try {
            val filteredPolls = mockPolls.filter { it.title.contains(query, ignoreCase = true) }
            searchRecommendRepository.getPolls(query)
            reduce {
                Success(
                    query = query,
                    results = filteredPolls,
                    searchRecommends = emptyList()
                )
            }
        } catch (e: Exception) {
            reduce { Error(query = query, message = "Ошибка поиска: ${e.message}") }
        }
    }

    private suspend fun handleSearchTextUpdate(text: String) = subIntent {
        if (text.isEmpty()) {
            loadInitialHistory()
            return@subIntent
        }

        autoSearchJob?.cancel()
        autoSearchJob = viewModelScope.launch {
            delay(2000)
            onEvent(SearchEvent.UpdateQuery(text))
            subIntent { postSideEffect(SearchEffect.AutoQuerySendEffect) }
        }

        try {
            val recommends = searchRecommendRepository.getQueryRecommends(text)
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
        searchRecommendRepository.deleteQueryFromHistory(query)

        try {
            val recommends = searchRecommendRepository.getQueryRecommends(
                query = state.query,
                useLastResponse = true,
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
}