package com.example.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.feature.auth.ui.home_screen.HomeScreen
import com.example.feature.auth.ui.search_screen.SearchScreen
import com.example.votiqua.core.ui_common.navigation.HomeRoute
import com.example.votiqua.core.ui_common.navigation.SearchScreenRoute
import org.koin.compose.viewmodel.koinViewModel

internal class RecomNavigatorImpl : RecomNavigator {
    override fun registerNavigation(
        navGraphBuilder: NavGraphBuilder,
        mainNavController: NavController
    ) {
        navGraphBuilder.apply {
            composable<SearchScreenRoute> {
                SearchScreen(
                    navController = mainNavController,
                    viewModel = koinViewModel(),
                )
            }

            composable<HomeRoute> {
                HomeScreen(
                    navController = mainNavController,
                )
            }
        }
    }
}