package org.example.votiqua

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.votiqua.ui.login_screen.LoginScreen
import org.example.votiqua.ui.navigation.LoginRoute
import org.example.votiqua.ui.navigation.MainScreenRoute
import org.example.votiqua.ui.navigation.RegisterRoute
import org.example.votiqua.ui.navigation.SplashRoute
import org.example.votiqua.ui.register_screen.RegisterScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val bottomNavController = rememberNavController()


        NavHost(navController = navController, startDestination = LoginRoute) {
            composable<LoginRoute> {
                LoginScreen(navController = navController)
            }

            composable<RegisterRoute> {
                RegisterScreen(navController, it.toRoute<RegisterRoute>().email)
            }

            composable<SplashRoute> {
            }

            composable<MainScreenRoute> {
            }
        }
    }
}