package org.example.votiqua.data

import com.example.common.di.MyDispatchers
import org.example.votiqua.data.network.SearchRecommendRemoteDataSource
import org.example.votiqua.data.repository.PollRepositoryImpl
import org.example.votiqua.data.repository.SearchRecommendRepository
import org.example.votiqua.domain.repository.PollRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun dataModule() = module {
    single<PollRepository> { PollRepositoryImpl(get(named(MyDispatchers.IO))) }

    single { SearchRecommendRepository(get(), get()) }
    single { SearchRecommendRemoteDataSource(get()) }
}