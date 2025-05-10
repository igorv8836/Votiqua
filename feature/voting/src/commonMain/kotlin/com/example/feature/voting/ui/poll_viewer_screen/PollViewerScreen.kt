package com.example.feature.voting.ui.poll_viewer_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feature.voting.ui.manage_poll_screen.elements.ParticipantsBlock
import com.example.votiqua.core.ui_common.constants.Dimens
import com.example.votiqua.core.ui_common.navigation.navigateToManagingPoll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PollViewerScreen(
    navController: NavController,
    viewModel: PollViewerViewModel,
    onClose: () -> Unit,
) {
    val state by viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is PollViewerSideEffect.EditRequested -> navController.navigateToManagingPoll(effect.pollId)
                PollViewerSideEffect.NavigateToLastScreen -> navController.popBackStack()
            }
        }
    }

    if (state.showExitDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowExitDialog(false) },
            title = { Text("Выход из голосования") },
            text = { Text("Вы уверены, что хотите выйти из голосования?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.confirmExiting()
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.setShowExitDialog(false) }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    if (state.isMember && !state.isMember) {
                        IconButton(onClick = { viewModel.setShowExitDialog(true) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Выйти из голосования"
                            )
                        }
                    }
                    if (state.isAdmin) {
                        IconButton(onClick = viewModel::onEditClicked) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Редактировать"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (!state.isMember) {
                JoinPollButton(onJoin = { viewModel.joinPoll() })
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PollViewerTopBlock(
                title = state.title,
                description = state.description,
                status = state.statusText,
                totalParticipants = state.memberCount,
                votersCount = state.voteCount,
                periodText = state.votingPeriod,
            )
            PollOptionsViewerBlock(
                state = state,
                onOptionSelected = { index ->
                    viewModel.selectOption(index)
                },
            )
            ParticipantsBlock(
                participants = state.participants,
                anonymous = state.anonymous,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun PollViewerTopBlock(
    title: String,
    description: String,
    status: String,
    totalParticipants: Int,
    periodText: String?,
    votersCount: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = "Статус",
                    value = status
                )
                InfoItem(
                    label = "Участники",
                    value = "$totalParticipants"
                )
                InfoItem(
                    label = "Проголосовали",
                    value = "$votersCount"
                )
            }
            periodText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun JoinPollButton(onJoin: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.small, vertical = Dimens.small),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onJoin,
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Присоединиться к голосованию",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}