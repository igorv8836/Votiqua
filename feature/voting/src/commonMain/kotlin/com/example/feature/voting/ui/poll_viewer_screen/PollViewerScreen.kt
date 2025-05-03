package com.example.feature.voting.ui.poll_viewer_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.feature.voting.ui.manage_poll_screen.elements.ParticipantsBlock
import com.example.votiqua.core.ui_common.constants.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PollViewerScreen(
    viewModel: PollViewerViewModel,
    onClose: () -> Unit,
    onEdit: () -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsState()
    var option by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                PollViewerSideEffect.EditRequested -> onEdit()
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
                status = "Активно",
                totalParticipants = state.participants.size,
                votersCount = state.participants.count { it.voted }
            )
            PollOptionsViewerBlock(
                options = state.options,
                voteCounts = state.options.mapIndexed { index, _ -> index },
                hasVoted = option != null,
                onOptionSelected = { index ->
                    option = index
                },
                selectedOption = option
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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
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
    options: List<String>,
    voteCounts: List<Int>,
    hasVoted: Boolean,
    onOptionSelected: (Int) -> Unit,
    selectedOption: Int? = null
) {
    if (hasVoted) {
        val totalVotes = voteCounts.sumOf { it }
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Варианты ответов",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.size(12.dp))
            options.forEachIndexed { index, option ->
                val votes = voteCounts.getOrElse(index) { 0 }
                val percentage = if (totalVotes > 0) votes.toFloat() / totalVotes else 0f
                val percentageText = if (totalVotes > 0) "${(percentage * 100).toInt()}%" else "0%"
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = percentageText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    LinearProgressIndicator(
                        progress = percentage,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.size(Dimens.medium))
            }
        }
    } else {
        Column(
            modifier = Modifier.padding(horizontal = Dimens.medium)
        ) {
            Text(
                text = "Варианты ответов",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.size(12.dp))
            options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOptionSelected(index)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = selectedOption == index,
                        onClick = { onOptionSelected(index) }
                    )
                    Spacer(modifier = Modifier.size(Dimens.small))
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.size(Dimens.medium))
            }
        }
    }
}