package org.example.votiqua.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.votiqua.ui.main_screen.HomeScreen
import org.example.votiqua.ui.search_screen.SearchScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(it)
        ) {
            composable<HomeRoute> {
                HomeScreen(navController)
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

            composable<SearchScreenRoute> {
                SearchScreen(navController)
            }
        }
    }
}