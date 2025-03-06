package org.example.votiqua.ui.voting_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Rtt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotingScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voting Event") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Voting Event Title", style = MaterialTheme.typography.headlineMedium)
                Image(imageVector = Icons.AutoMirrored.Default.Rtt, contentDescription = "Voting Image")
                Text(text = "This is a description of the voting event.", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Date: 2023-03-06", style = MaterialTheme.typography.bodySmall)
                LazyColumn {
                    items(listOf("Option 1", "Option 2", "Option 3")) { option ->
                        Text(text = option, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                LazyColumn {
                    items(listOf("Participant 1", "Participant 2", "Participant 3")) { participant ->
                        Text(text = participant, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Button(onClick = {  }) {
                    Text("Vote")
                }
                Button(onClick = {  }) {
                    Text("Cancel")
                }
            }
        }
    }
} 