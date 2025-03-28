package org.example.votiqua

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.common.ThemeMode
import com.example.orbit_mvi.compose.collectAsState
import org.example.votiqua.ui.AppViewModel
import org.example.votiqua.ui.navigation.AppNavigation
import org.example.votiqua.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun MyApp() {
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

        AppNavigation(navController)
    }
}