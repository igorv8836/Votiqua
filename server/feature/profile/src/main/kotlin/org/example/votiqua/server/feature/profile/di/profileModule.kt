package org.example.votiqua.server.feature.profile.di

import io.ktor.server.config.HoconApplicationConfig
import org.example.votiqua.server.feature.profile.data.ProfilePhotoUrlConverter
import org.example.votiqua.server.feature.profile.data.ProfilePhotoUrlConverterImpl
import org.example.votiqua.server.feature.profile.data.ProfileRepository
import org.example.votiqua.server.feature.profile.data.ProfileRepositoryImpl
import org.example.votiqua.server.feature.profile.domain.ProfilePhotoUseCase
import org.koin.dsl.module

fun profileModule() = module {
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    single<ProfilePhotoUrlConverter> {
        val config = get<HoconApplicationConfig>()
        val serverUrl = config.property("ktor.deployment.url").getString()
        ProfilePhotoUrlConverterImpl(serverUrl)
    }
    single { ProfilePhotoUseCase(get()) }
}