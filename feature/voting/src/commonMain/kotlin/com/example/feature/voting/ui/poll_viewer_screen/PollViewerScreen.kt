package com.example.feature.voting.ui.poll_viewer_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
            }
        }
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
                anonymous = state.anonymous
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
fun PollOptionsViewerBlock(
    state: PollViewerState,
    onOptionSelected: (Int) -> Unit,
) {
    if (state.selectedOption != null) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Варианты ответов",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            state.options.map {
                SelectedOption(
                    option = it,
                    state = state,
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.padding(horizontal = Dimens.medium)
        ) {
            Text(
                text = "Варианты ответов",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            state.options.map {
                UnselectedOption(
                    state = state,
                    option = it,
                    onOptionSelected = { onOptionSelected(it.id) }
                )
            }
        }
    }
}

@Composable
fun SelectedOption(
    option: OptionAndCounts,
    state: PollViewerState,
) {
    val votes = option.count
    val percentage = if (state.voteCount > 0) votes.toFloat() / state.voteCount else 0f
    val percentageText = if (state.voteCount > 0) "${(percentage * 100).toInt()}%" else "0%"
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.medium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.tiny),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = option.option,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = percentageText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        LinearProgressIndicator(
            progress = percentage,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun UnselectedOption(
    state: PollViewerState,
    option: OptionAndCounts,
    onOptionSelected: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(bottom = Dimens.medium)
            .clickable(enabled = state.votingAvailable) {
                onOptionSelected()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            enabled = false,
            selected = false,
            onClick = {  },
        )
        Text(
            text = option.option,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}