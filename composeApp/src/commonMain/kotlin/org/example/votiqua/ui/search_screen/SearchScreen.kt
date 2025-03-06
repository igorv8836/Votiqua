package org.example.votiqua.ui.search_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.example.votiqua.ui.common.AppPaddings
import org.example.votiqua.ui.common.AppSearchBar
import org.example.votiqua.ui.main_screen.PollCard
import org.example.votiqua.ui.main_screen.mockPolls

@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredPolls = mockPolls.filter { searchQuery.isNotEmpty() && it.title.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier.padding(
            vertical = AppPaddings.VERTICAL_PADDING,
            horizontal = AppPaddings.HORIZONTAL_PADDING,
        )
    ) {
        AppSearchBar(false, navController = navController, onQueryChanged = { query ->
            searchQuery = query
        })

        LazyColumn {
            item {
                Text(
                    text = "Найденные голосования",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(filteredPolls) { item ->
                PollCard(item)
            }
        }
    }
}