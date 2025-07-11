package com.example.feature.auth.ui.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feature.auth.ui.common.AppSearchBar
import com.example.feature.voting.domain.models.PollCardState
import com.example.feature.voting.ui.PollCard
import com.example.orbit_mvi.compose.collectAsState
import com.example.votiqua.core.ui_common.constants.AppPaddings
import com.example.votiqua.core.ui_common.constants.Dimens
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.collectAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        AppSearchBar(true, navController = navController)
        MessagesBlock(state.messages)
        PollsBlock(
            "Новые голосования",
            state.newPolls,
            navController,
            isHorizontal = true,
            onClickFavorite = { viewModel.onClickFavourite(it) },
        )
        PollsBlock(
            "Популярные голосования",
            state.popularPolls,
            navController,
            onClickFavorite = { viewModel.onClickFavourite(it) },
        )
    }
}

@Composable
fun MessagesBlock(notifications: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppPaddings.HORIZONTAL_PADDING,
            )
            .padding(top = 16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Новости",
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            notifications.forEach { notification ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            notification,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PollsBlock(
    title: String,
    polls: List<PollCardState>,
    navController: NavController,
    onClickFavorite: (Int) -> Unit,
    isHorizontal: Boolean = false
) {
    Column(
        modifier = Modifier.padding(top = Dimens.large)
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = AppPaddings.HORIZONTAL_PADDING)
        )
        if (isHorizontal) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = AppPaddings.HORIZONTAL_PADDING),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(polls) { poll ->
                    PollCard(
                        poll = poll,
                        navController = navController,
                        modifier = Modifier.width(240.dp),
                        onClickFavorite = { onClickFavorite(poll.id) },
                    )
                }
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = AppPaddings.HORIZONTAL_PADDING)) {
                polls.forEach { poll ->
                    PollCard(
                        poll = poll,
                        navController = navController,
                        onClickFavorite = { onClickFavorite(poll.id) },
                    )
                }
            }
        }
    }
}