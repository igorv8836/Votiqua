package com.example.feature.profile.ui.profile_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.feature.voting.domain.models.UiPoll
import com.example.feature.voting.ui.PollCard
import com.example.votiqua.core.ui_common.constants.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
) {
    val favoritePolls: List<UiPoll> = listOf(
        UiPoll("Голосование 1", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01"),
        UiPoll("Голосование 2", "2023-11-30", 50, "Закрыто", "Выбор подарка на ДР", "Подарки", "2023-09-01"),
        UiPoll("Голосование 2", "2023-11-30", 50, "Закрыто", "Выбор подарка на ДР", "Подарки", "2023-09-01"),
        UiPoll("Голосование 2", "2023-11-30", 50, "Закрыто", "Выбор подарка на ДР", "Подарки", "2023-09-01"),
        UiPoll("Голосование 2", "2023-11-30", 50, "Закрыто", "Выбор подарка на ДР", "Подарки", "2023-09-01"),
        UiPoll("Голосование 2", "2023-11-30", 50, "Закрыто", "Выбор подарка на ДР", "Подарки", "2023-09-01"),
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранное") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (favoritePolls.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет избранных голосований",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = Dimens.medium,
                    end = Dimens.medium,
                    top = innerPadding.calculateTopPadding(),
                    bottom = Dimens.medium
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens.small),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoritePolls) { poll ->
                    PollCard(
                        poll = poll,
                        navController = navController,
                        isLiked = true,
                    )
                }
            }
        }
    }
}