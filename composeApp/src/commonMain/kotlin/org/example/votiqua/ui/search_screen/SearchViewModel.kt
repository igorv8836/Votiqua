package org.example.votiqua.ui.search_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.orbit_mvi.viewmodel.container
import org.example.votiqua.data.repository.SearchRecommendRepository
import org.example.votiqua.ui.main_screen.Poll
import org.example.votiqua.ui.main_screen.mockPolls
import org.example.votiqua.ui.search_screen.SearchEvent.UpdateQuery
import org.example.votiqua.ui.search_screen.SearchState.Error
import org.example.votiqua.ui.search_screen.SearchState.Loading
import org.example.votiqua.ui.search_screen.SearchState.Success
import org.orbitmvi.orbit.ContainerHost

@Stable
sealed class SearchState {
    open val query: String = ""

    data class Loading(
        override val query: String,
    ) : SearchState()

    data class Success(
        override val query: String,
        val results: List<Poll> = emptyList(),
        val searchRecommends: List<String> = emptyList()
    ) : SearchState()

    data class Error(
        override val query: String,
        val message: String,
    ) : SearchState()
}

sealed interface SearchEvent {
    data class UpdateQuery(val query: String) : SearchEvent
    data class UpdateSearchText(val text: String) : SearchEvent
    object Retry : SearchEvent
}

sealed class SearchEffect {
}

class SearchViewModel(
    private val searchRecommendRepository: SearchRecommendRepository,
) : ViewModel(), ContainerHost<SearchState, SearchEffect> {
    override val container = container<SearchState, SearchEffect>(
        initialState = Success(query = "", results = emptyList())
    )

    fun onEvent(event: SearchEvent) {
        when (event) {
            is UpdateQuery -> intent {
                val query = event.query
                if (query.isEmpty()) {
                    reduce { Success(query = query, results = emptyList()) }
                } else {
                    reduce { Loading(query = query) }
                    try {
                        val filtered = mockPolls.filter { it.title.contains(query, ignoreCase = true) }
                        reduce { Success(query = query, results = filtered) }
                    } catch (e: Exception) {
                        reduce { Error(query = query, message = "Ошибка поиска: ${e.message}") }
                    }
                }
            }

            SearchEvent.Retry -> intent {
                onEvent(UpdateQuery(state.query))
            }

            is SearchEvent.UpdateSearchText -> intent {
                val query = event.text
                if (query.isEmpty()) {
                    reduce { Success(query = query, results = emptyList()) }
                } else {
                    try {
                        val recommends = searchRecommendRepository.searchPolls(query).results
                        reduce { Success(query = query, searchRecommends = recommends) }
                    } catch (e: Exception) {
                        reduce { Error(query = query, message = "Ошибка поиска: ${e.message}") }
                    }
                }
            }
        }
    }
}