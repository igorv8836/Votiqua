package org.example.votiqua.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.example.votiqua.di.appModule
import org.example.votiqua.server.feature.auth.di.authModule
import org.example.votiqua.server.feature.voting.di.votingModule
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            appModule(),
            authModule(),
            votingModule(),
        )
    }
}