package org.example.votiqua.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.example.votiqua.ui.login_screen.LoginScreen
import org.example.votiqua.ui.manage_poll_screen.ManagePollScreen
import org.example.votiqua.ui.poll_viewer_screen.PollViewerScreen
import org.example.votiqua.ui.profile_screen.FavoritesScreen
import org.example.votiqua.ui.register_screen.RegisterScreen
import org.example.votiqua.ui.search_screen.SearchScreen
import org.example.votiqua.ui.splash_screen.SplashScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MainScreenRoute) {
        composable<LoginRoute> {
            LoginScreen(
                navController = navController,
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(navController, it.toRoute<RegisterRoute>().email)
        }

        composable<SplashRoute> {
            SplashScreen(navController = navController)
        }

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