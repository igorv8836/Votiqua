package com.example.feature.auth.ui.search_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feature.auth.ui.common.AppSearchBar
import com.example.feature.voting.ui.PollCard
import com.example.orbit_mvi.compose.collectAsState
import com.example.orbit_mvi.compose.collectSideEffect
import com.example.votiqua.core.ui_common.constants.Dimens
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

    Scaffold {
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is SearchState.Error -> {
                    val errorState = state as SearchState.Error
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.onEvent(SearchEvent.UpdateQuery(errorState.query)) }) {
                            Text(text = "Обновить")
                        }
                    }
                }
                is SearchState.Success -> {
                    val successState = state as SearchState.Success
                    if (successState.query.isNotEmpty() && successState.results.isEmpty()) {
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
                                PollCard(poll = poll, navController = navController) {  }
                            }
                        }
                    }
                }
            }
        }
    }
}