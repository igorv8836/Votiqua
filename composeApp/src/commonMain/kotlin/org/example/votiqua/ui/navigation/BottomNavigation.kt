package org.example.votiqua.ui.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature.profile.api.navigation.ProfileNavigator
import com.example.feature.voting.navigation.VotingNavigator
import com.example.votiqua.core.ui_common.navigation.BottomPollCreateRoute
import com.example.votiqua.core.ui_common.navigation.HomeRoute
import com.example.votiqua.core.ui_common.navigation.NotificationsRoute
import com.example.votiqua.core.ui_common.navigation.navigateToCreate
import org.example.votiqua.ui.main_screen.HomeScreen
import org.example.votiqua.ui.notifications_screen.NotificationsScreen
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    mainNavController: NavController,
) {
    val bottomNavController = rememberNavController()
    val profileNavigator = koinInject<ProfileNavigator>()
    val votingNavigator = koinInject<VotingNavigator>()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = bottomNavController,
                mainNavController = mainNavController,
            )
        },
    ) {
        NavHost(
            navController = bottomNavController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(it)
                .systemBarsPadding()
                .navigationBarsPadding()
        ) {
            profileNavigator.registerBottomNavigation(this, mainNavController = mainNavController)
            votingNavigator.registerBottomNavigation(this, mainNavController = mainNavController)

            composable<HomeRoute> {
                HomeScreen(
                    bottomNavController = bottomNavController,
                    mainNavController = mainNavController,
                )
            }

            composable<BottomPollCreateRoute> {
                mainNavController.navigateToCreate()
            }

            composable<NotificationsRoute> {
                NotificationsScreen()
            }
        }
    }
}