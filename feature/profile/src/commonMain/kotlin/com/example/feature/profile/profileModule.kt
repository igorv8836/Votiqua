package com.example.feature.profile

import com.example.feature.profile.api.data.repository.ProfileRepository
import com.example.feature.profile.api.navigation.ProfileNavigator
import com.example.feature.profile.data.repository.ProfileRemoteDataSource
import com.example.feature.profile.data.repository.ProfileRepositoryImpl
import com.example.feature.profile.navigation.ProfileNavigatorImpl
import com.example.feature.profile.ui.favorites_screen.FavoritePollsViewModel
import com.example.feature.profile.ui.profile_screen.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun profileModule() = module {
    single<ProfileNavigator> { ProfileNavigatorImpl() }

    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }

    single<ProfileRemoteDataSource> { ProfileRemoteDataSource(get()) }

    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { FavoritePollsViewModel(get(), get(), get()) }
}

//expect fun profilePlatformModule()