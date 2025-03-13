package org.example.votiqua.ui.manage_poll_screen.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.example.votiqua.ui.manage_poll_screen.ManagePollState

@Composable
internal fun TopBlock(
    state: ManagePollState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onStartClicked: () -> Unit,
    onOpenClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onAnonClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .padding(vertical = 20.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChanged,
            label = { Text("Вопрос") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChanged,
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            ManagingButton(
                icon = Icons.Default.PlayArrow,
                text = "Запустить",
                isAbled = !state.votesExist,
                onClick = onStartClicked
            )

            ManagingButton(
                icon = if (state.isOpen) Icons.Default.Public else Icons.Default.Lock,
                text = if (state.isOpen) "Публичное" else "Закрытое",
                isAbled = !state.votesExist,
                onClick = onOpenClicked
            )

            ManagingButton(
                icon = if (state.anonymous) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                text = if (state.anonymous) "Анонимное" else "Неанонимное",
                isAbled = !state.votesExist,
                onClick = onAnonClicked
            )

            ManagingButton(
                icon = Icons.Default.DeleteOutline,
                text = "Удалить",
                isAbled = !state.votesExist,
                onClick = onDeleteClicked
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Ссылка на голосование",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.link,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Копировать ссылку",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Autorenew,
                        contentDescription = "Перегенерировать ссылку",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}