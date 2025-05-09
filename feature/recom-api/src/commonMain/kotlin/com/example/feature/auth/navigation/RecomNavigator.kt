package com.example.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface RecomNavigator {
    fun registerNavigation(
        navGraphBuilder: NavGraphBuilder,
        mainNavController: NavController
    )
}