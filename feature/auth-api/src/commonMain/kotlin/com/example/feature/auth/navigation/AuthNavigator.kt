package com.example.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface AuthNavigator {
    fun registerNavigation(
        navGraphBuilder: NavGraphBuilder,
        mainNavController: NavController
    )
}