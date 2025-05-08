package com.example.feature.voting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface VotingNavigator {
    fun registerNavigation(
        navGraphBuilder: NavGraphBuilder,
        mainNavController: NavController
    )
}