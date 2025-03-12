package org.example.votiqua

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.example.votiqua.ui.navigation.AppNavigation
import org.example.votiqua.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun MyApp() {
    AppTheme(darkTheme = false) {
        val navController = rememberNavController()

        AppNavigation(navController)
    }
}