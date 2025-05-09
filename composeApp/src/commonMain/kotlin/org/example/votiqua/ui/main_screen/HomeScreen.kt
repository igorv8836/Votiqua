package org.example.votiqua.ui.main_screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feature.voting.domain.models.PollCardState
import com.example.feature.voting.ui.PollCard
import com.example.votiqua.core.ui_common.constants.AppPaddings
import com.example.votiqua.core.ui_common.constants.Dimens
import org.example.votiqua.ui.common.AppSearchBar

val mockPolls = listOf(
    PollCardState(
        "Открытие Голосование",
        "2023-12-31",
        100,
        "Открыто",
        "Голосование в честь открытия",
        "Праздники",
        "2023-09-01",
        isFavorite = false,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
) {
    val notifications = listOf("Новое голосование доступно", "Ваше голосование завершено")
    val activePolls = listOf(
        PollCardState("Голосование 1", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01", false),
        PollCardState("Голосование 2", "2023-11-30", 50, "Закрыто", "Выбор подарка на ДР", "Подарки", "2023-09-01", false),
    )
    val myPolls = listOf(
        PollCardState("Мое голосование 1", "2023-10-15", 30, "Открыто", "Новый год", "Праздники", "2023-09-01", false),
        PollCardState("Мое голосование 2", "2023-09-20", 20, "Закрыто", "ПОдарок", "Подарки", "2023-09-01", false)
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        AppSearchBar(true, navController = navController)
        NotificationBlock(notifications)
        PollsBlock("Открытые голосования", activePolls, navController, isHorizontal = true)
        PollsBlock("Недавно открытые", myPolls, navController)
    }
}

@Composable
fun NotificationBlock(notifications: List<String>) {
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
                    "Уведомления",
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
                        modifier = Modifier.width(240.dp)
                    ) { }
                }
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = AppPaddings.HORIZONTAL_PADDING)) {
                polls.forEach { poll ->
                    PollCard(poll = poll, navController = navController) {  }
                }
            }
        }
    }
}