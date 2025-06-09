package org.example.votiqua

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.votiqua.core.ui_common.navigation.ScreenRoute
import com.example.common.ThemeMode
import com.example.orbit_mvi.compose.collectAsState
import com.example.votiqua.core.ui_common.theme.AppTheme
import org.example.votiqua.ui.AppViewModel
import org.example.votiqua.ui.navigation.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun MyApp(
    startRoute: ScreenRoute? = null,
    onNavController: (NavHostController) -> Unit = {}
) {
    val viewModel: AppViewModel = koinViewModel()
    val state = viewModel.collectAsState()

    AppTheme(
        darkTheme = when (state.value.themeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }
    ) {
        val navController = rememberNavController()
        onNavController(navController)
        LaunchedEffect(startRoute) {
            startRoute?.let { navController.navigate(it) }
        }
        AppNavigation(navController)
    }
}