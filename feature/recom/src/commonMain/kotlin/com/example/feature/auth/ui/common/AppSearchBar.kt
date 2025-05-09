package com.example.feature.auth.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.feature.auth.domain.QueryRecommendModel
import com.example.feature.auth.domain.QueryType
import com.example.feature.auth.ui.search_screen.SearchEvent
import com.example.votiqua.core.ui_common.constants.AppPaddings
import com.example.votiqua.core.ui_common.navigation.navigateToSearch
import org.jetbrains.compose.resources.stringResource
import votiqua.core.ui_common.generated.resources.Res
import votiqua.core.ui_common.generated.resources.back_or_search
import votiqua.core.ui_common.generated.resources.clear
import votiqua.core.ui_common.generated.resources.find_vote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    isMainScreen: Boolean,
    navController: NavController,
    recommends: List<QueryRecommendModel> = emptyList(),
    onQueryChanged: (String) -> Unit = { },
    onEvent: (SearchEvent) -> Unit = { },
    expanded: Boolean = false,
    changeExpanded: (Boolean) -> Unit = { },
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var textFieldValue by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .semantics { isTraversalGroup = true }
            .padding(
                horizontal = AppPaddings.HORIZONTAL_PADDING,
                vertical = AppPaddings.VERTICAL_PADDING,
            )
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
                        onEvent(SearchEvent.UpdateSearchText(it))
                    },
                    query = textFieldValue,
                    onSearch = {
                        changeExpanded(false)
                        onQueryChanged(textFieldValue)
                    },
                    expanded = expanded,
                    onExpandedChange = { if (!isMainScreen) changeExpanded(it) else navController.navigateToSearch() },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.find_vote),
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                            if (!isMainScreen) navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = if (!isMainScreen) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Search,
                                contentDescription = stringResource(Res.string.back_or_search)
                            )
                        }
                    },
                    trailingIcon = {
                        if (textFieldValue.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    textFieldValue = ""
                                    changeExpanded(false)
                                    onQueryChanged("")
                                    keyboardController?.hide()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(Res.string.clear)
                                )
                            }
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { changeExpanded(it) },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                recommends.forEach { model ->
                    ListItem(
                        leadingContent = {
                            val icon = when (model.type) {
                                QueryType.HISTORY -> Icons.Default.History
                                QueryType.RECOMMEND -> Icons.Default.Search
                                
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                            )
                        },
                        trailingContent = {
                            if (model.type == QueryType.HISTORY) {
                                IconButton(
                                    onClick = { onEvent(SearchEvent.DeleteQuery(model.query)) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                    )
                                }
                            }
                        },
                        headlineContent = { Text(model.query) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier =
                            Modifier.clickable {
                                textFieldValue = model.query
                                onQueryChanged(textFieldValue)
                                changeExpanded(false)
                            }
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                    )
                    HorizontalDivider(
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
        }
        if (isMainScreen) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .zIndex(1f)
                    .clickable { navController.navigateToSearch() }
            )
        }
    }
}