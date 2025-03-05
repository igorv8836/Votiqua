package org.example.votiqua.ui.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenRoute


fun NavController.navigateToMain() {
    navigate(MainScreenRoute)
}