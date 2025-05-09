package com.example.votiqua.core.ui_common.navigation

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
    navigate(HomeRoute)
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

@Serializable
data class ManagePollRoute(
    val pollId: Int?,
) : ScreenRoute

@Serializable
data class PollViewerRoute(
    val pollId: Int,
) : ScreenRoute

@Serializable
data object FavouriteScreenRoute : ScreenRoute

fun NavController.navigateToHome() = navigate(HomeRoute)
fun NavController.navigateToMyPolls() = navigate(MyPollsRoute)
fun NavController.navigateToCreate() = navigate(PollCreateRoute)
fun NavController.navigateToNotifications() = navigate(NotificationsRoute)
fun NavController.navigateToProfile() = navigate(ProfileRoute)
fun NavController.navigateToManagingPoll(pollId: Int? = null) = navigate(ManagePollRoute(pollId))
fun NavController.navigateToPollViewer(
    pollId: Int,
) = navigate(PollViewerRoute(pollId))
fun NavController.navigateToFavourite() = navigate(FavouriteScreenRoute)

