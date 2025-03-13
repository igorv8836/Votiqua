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
import org.example.votiqua.ui.main_screen.HomeScreen
import org.example.votiqua.ui.notifications_screen.NotificationsScreen
import org.example.votiqua.ui.poll_list_screen.PollListScreen
import org.example.votiqua.ui.profile_screen.ProfileScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    mainNavController: NavController,
) {
    val bottomNavController = rememberNavController()
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
            composable<HomeRoute> {
                HomeScreen(
                    bottomNavController = bottomNavController,
                    mainNavController = mainNavController,
                )
            }

            composable<MyPollsRoute> {
                PollListScreen(
                    navController = mainNavController
                )
            }

            composable<BottomPollCreateRoute> {
                mainNavController.navigateToCreate()
            }

            composable<NotificationsRoute> {
                NotificationsScreen()
            }

            composable<ProfileRoute> {
                ProfileScreen(
                    viewModel = koinViewModel(),
                    navController = mainNavController,
                )
            }
        }
    }
}