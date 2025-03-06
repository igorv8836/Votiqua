package org.example.votiqua.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem<T : Any>(val title:String, val icon: ImageVector, val route: T)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Main", Icons.Default.Home, HomeRoute),
        BottomNavItem("Votes", Icons.Default.List, MyPollsRoute),
        BottomNavItem("Create", Icons.Default.Add, PollCreateRoute),
        BottomNavItem("Notifications", Icons.Default.Notifications, NotificationsRoute),
        BottomNavItem("Profile", Icons.Default.Person, ProfileRoute)
    )
    NavigationBar(
//        windowInsets = NavigationBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal)
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
} 