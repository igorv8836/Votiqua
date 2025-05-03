package com.example.feature.voting.ui.manage_poll_screen.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.NotStarted
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.feature.voting.ui.manage_poll_screen.ManagePollState

@Composable
fun TopBlock(
    state: ManagePollState,
    isCreating: Boolean,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onStartClicked: () -> Unit,
    onOpenClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onAnonClicked: () -> Unit,
    onMultipleChoiceClicked: () -> Unit,
    onStartDateChanged: (Long) -> Unit,
    onStartTimeChanged: (Long) -> Unit,
    onEndTimeChanged: (Long) -> Unit,
    onEndDateChanged: (Long) -> Unit,
    onTagAdded: (String) -> Unit,
    onTagRemoved: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChanged,
            label = { Text("Вопрос") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChanged,
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DateField(
                label = "Дата запуска",
                value = state.startDate,
                onDateSelected = onStartDateChanged,
                modifier = Modifier.weight(1f)
            )
            DateField(
                label = "Дата завершения",
                value = state.endDate,
                onDateSelected = onEndDateChanged,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimeField(
                label = "Время запуска",
                value = state.startTime,
                onTimeSelected = onStartTimeChanged,
                modifier = Modifier.weight(1f)
            )
            TimeField(
                label = "Время завершения",
                value = state.endTime,
                onTimeSelected = onEndTimeChanged,
                modifier = Modifier.weight(1f)
            )
        }
        TagsSection(
            tags = state.tags,
            onTagAdded = onTagAdded,
            onTagRemoved = onTagRemoved
        )
        PollCheckBox(
            text = "Множественный выбор",
            checked = state.multipleChoice,
            onClick = onMultipleChoiceClicked
        )
        PollCheckBox(
            text = "Публичное голосование",
            checked = state.isOpen,
            onClick = onOpenClicked
        )
        PollCheckBox(
            text = "Анонимное голосование",
            checked = state.anonymous,
            onClick = onAnonClicked
        )
        LinkBox(state.link)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            ManagingButton(
                icon = Icons.Filled.NotStarted,
                text = "Запустить",
                isAbled = !isCreating,
                onClick = onStartClicked
            )
            ManagingButton(
                icon = Icons.Filled.DeleteOutline,
                text = "Удалить",
                isAbled = !isCreating,
                onClick = onDeleteClicked
            )
        }
    }
}