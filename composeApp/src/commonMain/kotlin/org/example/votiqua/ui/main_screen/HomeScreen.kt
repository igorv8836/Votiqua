package org.example.votiqua.ui.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.example.votiqua.ui.common.AppPaddings
import org.example.votiqua.ui.common.AppSearchBar
import org.example.votiqua.ui.common.PollCard

val mockPolls = listOf(
    Poll("Открытие Голосование", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01"),
    Poll("Выбор Подарка", "2023-11-30", 50, "Закрыто", "Выбор подарка на ДР", "Подарки", "2023-09-01"),
    Poll("Пикник Место", "2023-10-15", 75, "Открыто", "Выбор места для пикника", "Отдых", "2023-08-01"),
    Poll("Тема Вечеринки", "2023-09-25", 30, "Закрыто", "Выбор темы вечеринки", "Развлечения", "2023-07-01"),
    Poll("Новый Логотип", "2023-08-20", 120, "Открыто", "Выбор нового логотипа", "Работа", "2023-06-01"),
    Poll("Благотворительная Акция", "2023-07-10", 200, "Закрыто", "Выбор благотворительной акции", "Благотворительность", "2023-05-01")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    bottomNavController: NavController,
    mainNavController: NavController,
) {
    val notifications = listOf("Новое голосование доступно", "Ваше голосование завершено")
    val activePolls = listOf(
        Poll("Голосование 1", "2023-12-31", 100, "Открыто", "Голосование в честь открытия", "Праздники", "2023-09-01"),
        Poll("Голосование 2", "2023-11-30", 50, "Закрыто", "Выбор подарка на ДР", "Подарки", "2023-09-01"),
    )
    val myPolls = listOf(
        Poll("Мое голосование 1", "2023-10-15", 30, "Открыто", "Новый год", "Праздники", "2023-09-01"),
        Poll("Мое голосование 2", "2023-09-20", 20, "Закрыто", "ПОдарок", "Подарки", "2023-09-01")
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding).padding(
                    horizontal = AppPaddings.HORIZONTAL_PADDING,
                    vertical = AppPaddings.VERTICAL_PADDING,
                )
                .verticalScroll(rememberScrollState())
        ) {
            AppSearchBar(true, navController = bottomNavController)
            NotificationBlock(notifications)
            PollsBlock("Активные голосования", activePolls, mainNavController)
            PollsBlock("Мои голосования", myPolls, mainNavController)
        }
    }
}

@Composable
fun NotificationBlock(notifications: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
fun PollsBlock(title: String, polls: List<Poll>, navController: NavController) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        polls.forEach { poll ->
            PollCard(poll, navController)
        }
    }
}

@Stable
data class Poll(
    val title: String,
    val endDate: String,
    val participants: Int,
    val status: String,
    val description: String,
    val category: String,
    val creationDate: String,
)