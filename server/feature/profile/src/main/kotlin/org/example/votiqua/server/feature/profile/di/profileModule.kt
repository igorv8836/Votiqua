package org.example.votiqua.server.feature.profile.di

import org.example.votiqua.server.feature.profile.data.NotificationRepository
import org.example.votiqua.server.feature.profile.data.ProfileRepository
import org.example.votiqua.server.feature.profile.domain.ProfilePhotoUseCase
import org.koin.dsl.module

fun profileModule() = module {
    single { NotificationRepository() }
    single { ProfileRepository() }
    single { ProfilePhotoUseCase(get()) }
}