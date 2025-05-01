package org.example.votiqua.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.feature.auth.navigation.AuthNavigator
import com.example.votiqua.core.ui_common.navigation.FavouriteScreenRoute
import com.example.votiqua.core.ui_common.navigation.MainScreenRoute
import com.example.votiqua.core.ui_common.navigation.ManagePollRoute
import com.example.votiqua.core.ui_common.navigation.PollCreateRoute
import com.example.votiqua.core.ui_common.navigation.PollViewerRoute
import com.example.votiqua.core.ui_common.navigation.SearchScreenRoute
import com.example.votiqua.core.ui_common.navigation.SplashRoute
import com.example.votiqua.core.ui_common.navigation.navigateToManagingPoll
import org.example.votiqua.ui.manage_poll_screen.ManagePollScreen
import org.example.votiqua.ui.poll_viewer_screen.PollViewerScreen
import org.example.votiqua.ui.profile_screen.FavoritesScreen
import org.example.votiqua.ui.search_screen.SearchScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    val authNavigator: AuthNavigator = koinInject<AuthNavigator>()

    NavHost(navController = navController, startDestination = SplashRoute) {
        authNavigator.registerNavigation(this, navController)

        composable<MainScreenRoute> {
            MainScreen(navController)
        }

        composable<ManagePollRoute> {
            ManagePollScreen(
                isCreating = false,
                viewModel = koinViewModel(),
                onClose = {
                    navController.popBackStack()
                },
                onDeleted = {
                    navController.popBackStack()
                }
            )
        }

        composable<PollCreateRoute> {
            ManagePollScreen(
                isCreating = true,
                viewModel = koinViewModel(),
                onClose = {
                    navController.popBackStack()
                },
                onDeleted = {
                    navController.popBackStack()
                }
            )
        }

        composable<SearchScreenRoute> {
            SearchScreen(
                navController = navController,
                viewModel = koinViewModel(),
            )
        }

        composable<PollViewerRoute> {
            PollViewerScreen(
                viewModel = koinViewModel(),
                onClose = {
                    navController.popBackStack()
                },
                onEdit = {
                    navController.navigateToManagingPoll()
                }
            )
        }

        composable<FavouriteScreenRoute> {
            FavoritesScreen(navController)
        }
    }
}