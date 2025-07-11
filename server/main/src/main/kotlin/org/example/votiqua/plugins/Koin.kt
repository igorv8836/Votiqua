package org.example.votiqua.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.example.votiqua.di.appModule
import org.example.votiqua.server.feature.auth.di.authModule
import org.example.votiqua.server.feature.profile.di.profileModule
import org.example.votiqua.server.feature.recom.di.recomModule
import org.example.votiqua.server.feature.voting.di.votingModule
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            appModule(),
            authModule(),
            votingModule(),
            profileModule(),
            recomModule(),
        )
    }
}