package org.example.votiqua

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.votiqua.di.KoinFactory

fun main() {
    KoinFactory.setupKoin()
    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Votiqua",
        ) {
            App()
        }
    }
}