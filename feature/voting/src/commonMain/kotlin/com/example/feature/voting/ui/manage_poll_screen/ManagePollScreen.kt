package com.example.feature.voting.ui.manage_poll_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.feature.voting.ui.manage_poll_screen.elements.ParticipantsBlock
import com.example.feature.voting.ui.manage_poll_screen.elements.PollOptionsBlock
import com.example.feature.voting.ui.manage_poll_screen.elements.TopBlock
import com.example.orbit_mvi.compose.collectSideEffect
import com.example.votiqua.core.ui_common.screens.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ManagePollScreen(
    isCreating: Boolean,
    viewModel: ManagePollViewModel,
    onClose: () -> Unit,
    onDeleted: () -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsState()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ManagePollSideEffect.Saved -> onClose()
            is ManagePollSideEffect.Deleted -> onDeleted()
        }
    }

    if (state.isSaving) {
        LoadingScreen()
    }

    if (showDeleteConfirmation) {
        DeleteDialog(
            hideDialog = { showDeleteConfirmation = false },
            onDelete = { viewModel.deletePoll() },
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (!isCreating) "Редактирование" else "Создание") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::saveChanges) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Сохранить изменения"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopBlock(
                state = state,
                isCreating = isCreating,
                onTitleChanged = viewModel::onTitleChanged,
                onDescriptionChanged = viewModel::onDescriptionChanged,
                onStartClicked = viewModel::startPoll,
                onOpenClicked = viewModel::onOpenClicked,
                onDeleteClicked = { showDeleteConfirmation = true },
                onAnonClicked = viewModel::onAnonClicked,
                onMultipleChoiceClicked = viewModel::onMultipleChoiceClicked,
                onStartTimeChanged = viewModel::onStartTimeChanged,
                onStartDateChanged = viewModel::onStartDateChanged,
                onEndTimeChanged = viewModel::onEndTimeChanged,
                onEndDateChanged = viewModel::onEndDateChanged,
                onTagAdded = viewModel::onTagAdded,
                onTagRemoved = viewModel::onTagRemoved,
                onRegenerateLink = viewModel::regenerateLink,
                onCopyLink = { /* TODO */ }
            )

            PollOptionsBlock(
                options = state.options,
                votesExist = state.votesExist,
                onOptionChanged = viewModel::onOptionChanged,
                onOptionRemoved = viewModel::removeOption,
                onOptionAdded = viewModel::addOption
            )

            if (!isCreating) {
                ParticipantsBlock(
                    participants = state.participants,
                    anonymous = state.anonymous,
                )
            }
        }
    }
}

@Composable
fun DeleteDialog(
    hideDialog: () -> Unit,
    onDelete: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = hideDialog,
        title = { Text("Удаление опроса") },
        text = { Text("Вы уверены, что хотите удалить этот опрос?") },
        confirmButton = {
            TextButton(
                onClick = {
                    hideDialog()
                    onDelete()
                }
            ) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = hideDialog
            ) {
                Text("Отмена")
            }
        }
    )
}