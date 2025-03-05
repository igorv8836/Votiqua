package org.example.votiqua

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.votiqua.ui.login_screen.LoginScreen
import org.example.votiqua.ui.main_screen.HomeScreen
import org.example.votiqua.ui.navigation.LoginRoute
import org.example.votiqua.ui.navigation.MainScreenRoute
import org.example.votiqua.ui.navigation.RegisterRoute
import org.example.votiqua.ui.navigation.SearchScreenRoute
import org.example.votiqua.ui.navigation.SplashRoute
import org.example.votiqua.ui.register_screen.RegisterScreen
import org.example.votiqua.ui.splash_screen.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val bottomNavController = rememberNavController()

        val snackBarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) {
            NavHost(navController = navController, startDestination = SplashRoute) {
                composable<LoginRoute> {
                    LoginScreen(
                        navController = navController,
                        snackBarHostState = snackBarHostState,
                    )
                }

                composable<RegisterRoute> {
                    RegisterScreen(navController, it.toRoute<RegisterRoute>().email)
                }

                composable<SplashRoute> {
                    SplashScreen(navController = navController)
                }

                composable<MainScreenRoute> {
                    HomeScreen(navController)
                }

                composable<SearchScreenRoute> {

                }
            }
        }

    }
}