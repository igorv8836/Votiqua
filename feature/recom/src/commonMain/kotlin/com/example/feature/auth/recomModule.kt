package com.example.feature.auth

import com.example.feature.auth.data.network.SearchRecommendRemoteDataSource
import com.example.feature.auth.data.repository.SearchRecommendRepository
import com.example.feature.auth.navigation.RecomNavigator
import com.example.feature.auth.navigation.RecomNavigatorImpl
import com.example.feature.auth.ui.home_screen.HomeViewModel
import com.example.feature.auth.ui.search_screen.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun recomModule() = module {
    single<RecomNavigator> { RecomNavigatorImpl() }
    single { SearchRecommendRepository(get(), get()) }
    single { SearchRecommendRemoteDataSource(get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}