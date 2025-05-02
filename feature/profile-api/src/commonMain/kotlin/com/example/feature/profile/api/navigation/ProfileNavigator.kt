package com.example.feature.profile.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface ProfileNavigator {
    fun registerNavigation(
        navGraphBuilder: NavGraphBuilder,
        mainNavController: NavController
    )

    fun registerBottomNavigation(
        navGraphBuilder: NavGraphBuilder,
        mainNavController: NavController
    )
}