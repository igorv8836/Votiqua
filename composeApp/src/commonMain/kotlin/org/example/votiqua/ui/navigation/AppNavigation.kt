package org.example.votiqua.ui.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.feature.auth.navigation.AuthNavigator
import com.example.feature.profile.api.navigation.ProfileNavigator
import com.example.feature.voting.navigation.VotingNavigator
import com.example.votiqua.core.ui_common.navigation.HomeRoute
import com.example.votiqua.core.ui_common.navigation.NotificationsRoute
import com.example.votiqua.core.ui_common.navigation.SearchScreenRoute
import com.example.votiqua.core.ui_common.navigation.SplashRoute
import org.example.votiqua.ui.main_screen.HomeScreen
import org.example.votiqua.ui.notifications_screen.NotificationsScreen
import org.example.votiqua.ui.search_screen.SearchScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    val authNavigator: AuthNavigator = koinInject<AuthNavigator>()
    val profileNavigator: ProfileNavigator = koinInject<ProfileNavigator>()
    val votingNavigator: VotingNavigator = koinInject<VotingNavigator>()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SplashRoute,
            modifier = Modifier
                .padding(innerPadding)
                .systemBarsPadding()
                .navigationBarsPadding()
        ) {
            authNavigator.registerNavigation(this, navController)
            profileNavigator.registerNavigation(this, navController)
            votingNavigator.registerNavigation(this, navController)

            composable<SearchScreenRoute> {
                SearchScreen(
                    navController = navController,
                    viewModel = koinViewModel(),
                )
            }

            composable<HomeRoute> {
                HomeScreen(
                    navController = navController,
                )
            }

            composable<NotificationsRoute> {
                NotificationsScreen()
            }
        }
    }
}