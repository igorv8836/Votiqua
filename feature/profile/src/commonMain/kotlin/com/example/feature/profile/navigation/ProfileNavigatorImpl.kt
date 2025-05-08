package com.example.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.feature.profile.api.navigation.ProfileNavigator
import com.example.feature.profile.ui.profile_screen.FavoritesScreen
import com.example.feature.profile.ui.profile_screen.ProfileScreen
import com.example.votiqua.core.ui_common.navigation.FavouriteScreenRoute
import com.example.votiqua.core.ui_common.navigation.ProfileRoute
import org.koin.compose.viewmodel.koinViewModel

internal class ProfileNavigatorImpl : ProfileNavigator {
    override fun registerNavigation(
        navGraphBuilder: NavGraphBuilder,
        mainNavController: NavController
    ) {
        navGraphBuilder.apply {
            composable<FavouriteScreenRoute> {
                FavoritesScreen(mainNavController)
            }

            composable<ProfileRoute> {
                ProfileScreen(
                    viewModel = koinViewModel(),
                    navController = mainNavController,
                )
            }
        }
    }
}