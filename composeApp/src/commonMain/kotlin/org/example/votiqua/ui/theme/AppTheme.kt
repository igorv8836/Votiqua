package org.example.votiqua.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    androidx.compose.material3.MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}