package com.example.feature.voting.ui.manage_poll_screen.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeField(
    label: String,
    value: String?,
    onTimeSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimeDialog by remember { mutableStateOf(false) }
    val nowLocal = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val timePickerState = rememberTimePickerState(initialHour = nowLocal.hour, initialMinute = nowLocal.minute)

    if (showTimeDialog) {
        Dialog(
            onDismissRequest = { showTimeDialog = false }
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors().copy(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            clockDialColor = MaterialTheme.colorScheme.surfaceContainer,
                        )
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showTimeDialog = false }
                        ) {
                            Text("Отмена")
                        }
                        TextButton(
                            onClick = {
                                val h = timePickerState.hour
                                val m = timePickerState.minute
                                onTimeSelected((h * 3600 + m * 60) * 1000L)
                                showTimeDialog = false
                            }
                        ) {
                            Text("ОК")
                        }
                    }
                }
            }
        }
    }

    OutlinedTextField(
        value = value.orEmpty(),
        onValueChange = {},
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        readOnly = true,
        enabled = false,
        colors = activeTextFieldColors(),
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            showTimeDialog = true
        }
    )
}