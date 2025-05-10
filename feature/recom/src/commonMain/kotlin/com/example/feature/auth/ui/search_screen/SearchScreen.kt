package com.example.feature.auth.ui.search_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feature.auth.ui.common.AppSearchBar
import com.example.feature.voting.ui.PollCard
import com.example.orbit_mvi.compose.collectAsState
import com.example.orbit_mvi.compose.collectSideEffect
import com.example.votiqua.core.ui_common.constants.Dimens
import com.example.votiqua.core.ui_common.screens.ErrorScreen
import com.example.votiqua.core.ui_common.screens.LoadingScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    var expanded by rememberSaveable { mutableStateOf(false) }

    viewModel.collectSideEffect {
        when (it) {
            SearchEffect.AutoQuerySendEffect -> expanded = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppSearchBar(
            isMainScreen = false,
            navController = navController,
            onQueryChanged = { query ->
                viewModel.onEvent(SearchEvent.UpdateQuery(query))
            },
            recommends = (state as? SearchState.Success)?.searchRecommends ?: emptyList(),
            onEvent = viewModel::onEvent,
            expanded = expanded,
            changeExpanded = { expanded = it }
        )
        when (state) {
            is SearchState.Loading -> {
                LoadingScreen()
            }
            is SearchState.Error -> {
                val errorState = state as SearchState.Error
                ErrorScreen(
                    errorMessage = errorState.message,
                    onRetry = { viewModel.onEvent(SearchEvent.UpdateQuery(errorState.query)) }
                )
            }
            is SearchState.Success -> {
                val successState = state as SearchState.Success
                if (successState.query.isNotEmpty() && successState.results.isEmpty()) {
                    EmptyScreen()
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = Dimens.medium)
                    ) {
                        item {
                            Text(
                                text = "Найденные голосования",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(successState.results) { poll ->
                            PollCard(
                                poll = poll,
                                navController = navController,
                                onClickFavorite = { viewModel.onClickFavourite(poll.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Ничего не найдено",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}