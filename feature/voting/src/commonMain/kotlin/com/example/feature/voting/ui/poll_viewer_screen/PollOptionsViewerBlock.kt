package com.example.feature.voting.ui.poll_viewer_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.votiqua.core.ui_common.constants.Dimens

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