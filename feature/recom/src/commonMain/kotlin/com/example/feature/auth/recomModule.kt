package com.example.feature.auth

import com.example.feature.auth.data.network.RecomRemoteDataSource
import com.example.feature.auth.data.repository.RecomRepository
import com.example.feature.auth.navigation.RecomNavigator
import com.example.feature.auth.navigation.RecomNavigatorImpl
import com.example.feature.auth.ui.home_screen.HomeViewModel
import com.example.feature.auth.ui.search_screen.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun recomModule() = module {
    single<RecomNavigator> { RecomNavigatorImpl() }
    single { RecomRepository(get(), get()) }
    single { RecomRemoteDataSource(get()) }

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
}