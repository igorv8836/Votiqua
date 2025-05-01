package org.example.votiqua

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.votiqua.di.KoinFactory

fun main() {
    KoinFactory.setupKoin()
    return application {
        val state = rememberWindowState()
        state.size = DpSize(
            width = Dp(400f),
            height = Dp(800f),
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "Votiqua",
            state = state,
        ) {
            MyApp()
        }
    }
}