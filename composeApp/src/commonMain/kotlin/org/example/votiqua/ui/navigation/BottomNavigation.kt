package org.example.votiqua.ui.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.votiqua.ui.main_screen.HomeScreen

@Composable
fun MainScreen(
    mainNavController: NavController,
) {
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(bottomNavController) },
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
                Text("MyPollsRoute")
            }

            composable<PollCreateRoute> {
                Text("PollCreateRoute")
            }

            composable<NotificationsRoute> {
                Text("NotificationsRoute")
            }

            composable<ProfileRoute> {
                Text("ProfileRoute")
            }
        }
    }
}