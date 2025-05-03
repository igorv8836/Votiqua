package org.example.votiqua.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.feature.auth.navigation.AuthNavigator
import com.example.feature.profile.api.navigation.ProfileNavigator
import com.example.feature.voting.navigation.VotingNavigator
import com.example.votiqua.core.ui_common.navigation.MainScreenRoute
import com.example.votiqua.core.ui_common.navigation.SearchScreenRoute
import com.example.votiqua.core.ui_common.navigation.SplashRoute
import org.example.votiqua.ui.search_screen.SearchScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    val authNavigator: AuthNavigator = koinInject<AuthNavigator>()
    val profileNavigator: ProfileNavigator = koinInject<ProfileNavigator>()
    val votingNavigator: VotingNavigator = koinInject<VotingNavigator>()

//    NavHost(navController = navController, startDestination = ManagePollRoute(pollId = null)) {
    NavHost(navController = navController, startDestination = SplashRoute) {
        authNavigator.registerNavigation(this, navController)
        profileNavigator.registerNavigation(this, navController)
        votingNavigator.registerNavigation(this, navController)

        composable<MainScreenRoute> {
            MainScreen(navController)
        }

        composable<SearchScreenRoute> {
            SearchScreen(
                navController = navController,
                viewModel = koinViewModel(),
            )
        }
    }
}