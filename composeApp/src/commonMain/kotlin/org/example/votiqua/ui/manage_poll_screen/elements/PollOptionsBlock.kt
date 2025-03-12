package org.example.votiqua.ui.manage_poll_screen.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PollOptionsBlock(
    options: List<String>,
    votesExist: Boolean,
    onOptionChanged: (Int, String) -> Unit,
    onOptionRemoved: (Int) -> Unit,
    onOptionAdded: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
        Text(
            text = "Варианты ответов",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        options.forEachIndexed { index, optionText ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = optionText,
                    onValueChange = { onOptionChanged(index, it) },
                    modifier = Modifier.weight(1f),
                    enabled = !votesExist,
                    placeholder = { Text("Введите вариант ответа") },
                )

                if (!votesExist && options.size > 2) {
                    IconButton(onClick = { onOptionRemoved(index) }) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Удалить вариант",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        if (!votesExist) {
            TextButton(
                onClick = onOptionAdded,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Добавить вариант")
            }
        }
    }
}
