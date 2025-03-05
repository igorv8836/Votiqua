package org.example.votiqua.ui.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenRoute

@Serializable
data object SearchScreenRoute


fun NavController.navigateToMain() {
    navigate(MainScreenRoute)
}

fun NavController.navigateToSearch() {
    navigate(SearchScreenRoute)
}