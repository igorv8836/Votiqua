package com.example.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.feature.auth.ui.login_screen.LoginScreen
import com.example.feature.auth.ui.register_screen.RegisterScreen
import com.example.feature.auth.ui.splash_screen.SplashScreen
import com.example.votiqua.core.ui_common.navigation.LoginRoute
import com.example.votiqua.core.ui_common.navigation.RegisterRoute
import com.example.votiqua.core.ui_common.navigation.SplashRoute

internal class AuthNavigatorImpl : AuthNavigator {
    override fun registerNavigation(
        navGraphBuilder: NavGraphBuilder,
        mainNavController: NavController
    ) {
        navGraphBuilder.apply {
            composable<LoginRoute> {
                LoginScreen(navController = mainNavController)
            }

            composable<RegisterRoute> {
                RegisterScreen(mainNavController, it.toRoute<RegisterRoute>().email)
            }

            composable<SplashRoute> {
                SplashScreen(navController = mainNavController)
            }
        }
    }
}