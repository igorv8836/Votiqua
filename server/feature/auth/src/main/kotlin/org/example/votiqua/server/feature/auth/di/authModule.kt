package org.example.votiqua.server.feature.auth.di

import io.ktor.server.config.*
import org.example.votiqua.server.feature.auth.data.EmailService
import org.example.votiqua.server.feature.auth.data.JwtService
import org.example.votiqua.server.feature.auth.data.PasswordResetRepository
import org.example.votiqua.server.feature.auth.data.UserRepository
import org.example.votiqua.server.feature.auth.domain.usecases.JwtUseCase
import org.example.votiqua.server.feature.auth.domain.usecases.PasswordResetUseCase
import org.example.votiqua.server.feature.auth.domain.usecases.UserUseCase
import org.example.votiqua.server.feature.auth.utils.HashFactory
import org.koin.dsl.module

fun authModule() = module {
    single {
        val config = get<HoconApplicationConfig>()
        HashFactory(
            config.property("auth.hashKey").getString().toByteArray()
        )
    }

    single {
        val config = get<HoconApplicationConfig>()
        JwtService(
            config.property("auth.jwtSecret").getString(),
        )
    }

    single { UserRepository() }
    single { PasswordResetRepository() }
    single { UserUseCase(get(), get(), get()) }
    single { JwtUseCase(get()) }
    single { PasswordResetUseCase(get(), get(), get(), get()) }
    single { EmailService(get()) }
}