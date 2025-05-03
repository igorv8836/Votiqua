package org.example.votiqua.data

import org.example.votiqua.data.network.SearchRecommendRemoteDataSource
import org.example.votiqua.data.repository.SearchRecommendRepository
import org.koin.dsl.module

fun dataModule() = module {
    single { SearchRecommendRepository(get(), get()) }
    single { SearchRecommendRemoteDataSource(get()) }
}