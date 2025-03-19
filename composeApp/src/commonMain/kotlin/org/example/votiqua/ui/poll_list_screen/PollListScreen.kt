package org.example.votiqua.ui.poll_list_screen

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.example.votiqua.ui.common.Dimens
import org.example.votiqua.ui.common.PollCard
import org.example.votiqua.ui.main_screen.Poll

@Composable
fun PollListScreen(
    navController: NavController,
) {
    val activePolls = listOf(
        Poll("Голосование 1", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01"),
        Poll("Голосование 1", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01"),
        Poll("Голосование 1", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01"),
        Poll("Голосование 1", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01"),
        Poll("Голосование 1", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01"),
        Poll("Голосование 1", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01"),
    )
    val myPolls = listOf(
        Poll("Мое голосование 1", "2023-10-15", 30, "Открыто", "Новый год", "Праздники", "2023-09-01"),
        Poll("Мое голосование 2", "2023-09-20", 20, "Закрыто", "ПОдарок", "Подарки", "2023-09-01"),
        Poll("Мое голосование 2", "2023-09-20", 20, "Закрыто", "ПОдарок", "Подарки", "2023-09-01"),
        Poll("Мое голосование 2", "2023-09-20", 20, "Закрыто", "ПОдарок", "Подарки", "2023-09-01"),
        Poll("Мое голосование 2", "2023-09-20", 20, "Закрыто", "ПОдарок", "Подарки", "2023-09-01"),
        Poll("Мое голосование 2", "2023-09-20", 20, "Закрыто", "ПОдарок", "Подарки", "2023-09-01"),
    )

    val tabList = listOf("Мои", "Остальные")
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

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
                    0 -> items(activePolls) { it1 ->
                        PollCard(it1, navController = navController)
                    }

                    1 -> items(myPolls) { it1 ->
                        PollCard(it1, navController = navController)
                    }
                }
            }
        }
    }
}