package org.example.votiqua.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.example.votiqua.ui.main_screen.mockPolls
import org.example.votiqua.ui.navigation.navigateToSearch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    isMainScreen: Boolean,
    navController: NavController,
    onQueryChanged: (String) -> Unit = { },
) {
    var textFieldValue by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val suggestions = mockPolls.map { it.title }
    val filteredSuggestions = suggestions.filter { it.contains(textFieldValue, ignoreCase = true) }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f }
                .fillMaxWidth(),
            inputField = {
                SearchBarDefaults.InputField(
                    onQueryChange = {
                        textFieldValue = it
                    },
                    query = textFieldValue,
                    onSearch = {
                        expanded = false
                        onQueryChanged(textFieldValue)
                               },
                    expanded = expanded,
                    onExpandedChange = { if (!isMainScreen) expanded = it else navController.navigateToSearch() },
                    placeholder = { Text("Найдите голосование") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                filteredSuggestions.forEach { resultText ->
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = { Text("Additional info") },
                        leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier =
                            Modifier.clickable {
                                textFieldValue = resultText
                                onQueryChanged(textFieldValue)
                                expanded = false
                            }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}