package org.example.votiqua.ui.manage_poll_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.votiqua.ui.manage_poll_screen.elements.ParticipantsBlock
import org.example.votiqua.ui.manage_poll_screen.elements.PollOptionsBlock
import org.example.votiqua.ui.manage_poll_screen.elements.TopBlock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ManagePollScreen(
    viewModel: ManagePollViewModel,
    onClose: () -> Unit,
    onDeleted: () -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is ManagePollSideEffect.Saved -> onClose()
                is ManagePollSideEffect.Deleted -> onDeleted()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Редактировать голосование") },
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
        }
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
                onTitleChanged = viewModel::onTitleChanged,
                onDescriptionChanged = viewModel::onDescriptionChanged,
                onStartClicked = {},
                onOpenClicked = {},
                onDeleteClicked = viewModel::requestDelete
            )

            PollOptionsBlock(
                options = state.options,
                votesExist = state.votesExist,
                onOptionChanged = viewModel::onOptionChanged,
                onOptionRemoved = viewModel::removeOption,
                onOptionAdded = viewModel::addOption
            )

            ParticipantsBlock(
                participants = state.participants,
                anonymous = state.anonymous,
            )
        }
    }
}