package com.example.feature.voting.ui.poll_list_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.feature.voting.ui.PollCard
import com.example.feature.voting.ui.manage_poll_screen.elements.PlaceholderScreen
import com.example.orbit_mvi.compose.collectAsState
import com.example.votiqua.core.ui_common.constants.Dimens
import com.example.votiqua.core.ui_common.screens.LoadingScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun PollListScreen(
    navController: NavController,
    viewModel: PollListViewModel = koinViewModel(),
) {
    val tabList = listOf("Мои", "Остальные")
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    val state by viewModel.collectAsState()

    Column(modifier = Modifier) {
        TabRow(selectedTabIndex = tabIndex) {
            tabList.forEachIndexed { index, s ->
                Tab(selected = tabIndex == index, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }, text = { Text(text = s) })
            }
        }
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { index ->
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = Dimens.medium)
            ) {
                when (index) {
                    0 -> {
                        if (state.myPollsIsLoading) {
                            item { LoadingScreen() }
                        } else if (state.myPolls.isEmpty()) {
                            item { PlaceholderScreen("Голосований пока нет") }
                        } else {
                            items(state.myPolls) { it1 ->
                                PollCard(it1, navController = navController) {
                                    viewModel.onClickFavourite(it1.id)
                                }
                            }
                        }
                    }

                    1 -> {
                        if (state.otherPollsIsLoading) {
                            item { LoadingScreen() }
                        } else if (state.otherPolls.isEmpty()) {
                            item { PlaceholderScreen("Голосований пока нет") }
                        } else {
                            items(state.otherPolls) { it1 ->
                                PollCard(it1, navController = navController) {
                                    viewModel.onClickFavourite(it1.id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}