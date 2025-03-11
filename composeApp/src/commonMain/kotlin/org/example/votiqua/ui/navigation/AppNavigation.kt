package org.example.votiqua.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.example.votiqua.ui.login_screen.LoginScreen
import org.example.votiqua.ui.manage_poll_screen.ManagePollScreen
import org.example.votiqua.ui.register_screen.RegisterScreen
import org.example.votiqua.ui.splash_screen.SplashScreen
import org.example.votiqua.ui.voting_screen.VotingScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ManagePollRoute) {
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

        composable<VotingRoute> {
            VotingScreen(
                navController = navController,
            )
        }

        composable<ManagePollRoute> {
            ManagePollScreen(
                viewModel = koinViewModel(),
                onClose = {},
                onDeleted = {}
            )
        }
    }
}