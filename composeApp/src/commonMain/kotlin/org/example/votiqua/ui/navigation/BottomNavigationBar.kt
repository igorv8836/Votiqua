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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.votiqua.core.ui_common.navigation.HomeRoute
import com.example.votiqua.core.ui_common.navigation.LoginRoute
import com.example.votiqua.core.ui_common.navigation.MyPollsRoute
import com.example.votiqua.core.ui_common.navigation.NotificationsRoute
import com.example.votiqua.core.ui_common.navigation.PollCreateRoute
import com.example.votiqua.core.ui_common.navigation.ProfileRoute
import com.example.votiqua.core.ui_common.navigation.RegisterRoute
import com.example.votiqua.core.ui_common.navigation.SplashRoute

data class BottomNavItem<T : Any>(val title:String, val icon: ImageVector, val route: T)

val items = listOf(
    BottomNavItem("Main", Icons.Default.Home, HomeRoute),
    BottomNavItem("Votes", Icons.Default.List, MyPollsRoute),
    BottomNavItem("Create", Icons.Default.Add, PollCreateRoute),
    BottomNavItem("Notifications", Icons.Default.Notifications, NotificationsRoute),
    BottomNavItem("Profile", Icons.Default.Person, ProfileRoute)
)

val noBottomBarRoutes = listOf(
    SplashRoute,
    RegisterRoute(),
    LoginRoute,
)

@Composable
fun BottomNavigationBar(
    navController: NavController,
) {
    var showBar by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(0) }
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    LaunchedEffect(currentDestination) {
        val index = items.indexOfFirst { item ->
            currentDestination?.hasRoute(item.route::class) == true
        }
        if (index != -1) {
            selectedItem = index
        }

        val hiddenBar = noBottomBarRoutes.any {
            currentDestination?.hasRoute(it::class) == true
        }

        showBar = !hiddenBar
    }

    if (showBar) {
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}