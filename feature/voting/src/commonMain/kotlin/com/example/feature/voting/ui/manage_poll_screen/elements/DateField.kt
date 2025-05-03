package com.example.feature.voting.ui.manage_poll_screen.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    label: String,
    value: String?,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDateDialog by remember { mutableStateOf(false) }
    val now = Clock.System.now()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = now.toEpochMilliseconds())

    if (showDateDialog) {
        Dialog(
            onDismissRequest = { showDateDialog = false },
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
            ) {
                Column {
                    DatePicker(
                        state = datePickerState,
                        colors = DatePickerDefaults.colors().copy(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainer
                            )
                    ) {
                        TextButton(
                            onClick = { showDateDialog = false }
                        ) {
                            Text("Отмена")
                        }
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { ms ->
                                    onDateSelected(ms)
                                }
                                showDateDialog = false
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
        enabled = false,
        readOnly = true,
        colors = activeTextFieldColors(),
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            showDateDialog = true
        }
    )
}

@Composable
internal fun activeTextFieldColors() = OutlinedTextFieldDefaults.colors(
    disabledContainerColor = OutlinedTextFieldDefaults.colors().unfocusedContainerColor,
    disabledBorderColor = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor,
    disabledTextColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor,
    disabledLabelColor = OutlinedTextFieldDefaults.colors().unfocusedLabelColor,
    disabledLeadingIconColor = OutlinedTextFieldDefaults.colors().unfocusedLeadingIconColor,
    disabledTrailingIconColor = OutlinedTextFieldDefaults.colors().unfocusedTrailingIconColor,
    disabledPlaceholderColor = OutlinedTextFieldDefaults.colors().unfocusedPlaceholderColor
)