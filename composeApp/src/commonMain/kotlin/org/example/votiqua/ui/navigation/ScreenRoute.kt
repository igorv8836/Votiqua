package org.example.votiqua.ui.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

interface ScreenRoute

@Serializable
data object LoginRoute : ScreenRoute

@Serializable
data object SplashRoute : ScreenRoute

@Serializable
data class RegisterRoute(val email: String? = null) : ScreenRoute

@Serializable
data object MainScreenRoute : ScreenRoute

@Serializable
data object SearchScreenRoute : ScreenRoute

fun NavController.navigateToLogin() = navigate(LoginRoute)
fun NavController.navigateToRegister(email: String?) = navigate(RegisterRoute(email))

fun NavController.navigateToMain() {
    navigate(MainScreenRoute)
}

fun NavController.navigateToSearch() {
    navigate(SearchScreenRoute)
}

@Serializable
data object HomeRoute : ScreenRoute

@Serializable
data object MyPollsRoute : ScreenRoute

@Serializable
data object PollCreateRoute : ScreenRoute

@Serializable
data object NotificationsRoute : ScreenRoute

@Serializable
data object ProfileRoute : ScreenRoute

fun NavController.navigateToHome() = navigate(HomeRoute)
fun NavController.navigateToMyPolls() = navigate(MyPollsRoute)
fun NavController.navigateToCreate() = navigate(PollCreateRoute)
fun NavController.navigateToNotifications() = navigate(NotificationsRoute)
fun NavController.navigateToProfile() = navigate(ProfileRoute)
