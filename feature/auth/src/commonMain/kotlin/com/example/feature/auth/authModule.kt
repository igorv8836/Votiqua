package com.example.feature.auth

import com.example.feature.auth.data.AuthRemoteDataSource
import com.example.feature.auth.data.AuthRepositoryImpl
import com.example.feature.auth.data.repository.AuthRepository
import com.example.feature.auth.navigation.AuthNavigator
import com.example.feature.auth.navigation.AuthNavigatorImpl
import com.example.feature.auth.ui.login_screen.LoginViewModel
import com.example.feature.auth.ui.register_screen.RegisterViewModel
import com.example.feature.auth.ui.splash_screen.SplashViewModel
import com.example.votiqua.datastore.auth.TokenDataStore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun authModule() = module {
    single<AuthRemoteDataSource> { AuthRemoteDataSource(get()) }
    single<TokenDataStore> { TokenDataStore(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    single<AuthNavigator> { AuthNavigatorImpl() }

    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { SplashViewModel(get()) }
}