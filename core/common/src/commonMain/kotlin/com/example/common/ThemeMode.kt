package com.example.common

enum class ThemeMode(val value: Int) {
    LIGHT(1),
    DARK(2),
    SYSTEM(3),
}

fun Int.toThemeMode(): ThemeMode {
    return when (this) {
        1 -> ThemeMode.LIGHT
        2 -> ThemeMode.DARK
        3 -> ThemeMode.SYSTEM
        else -> ThemeMode.SYSTEM
    }
}