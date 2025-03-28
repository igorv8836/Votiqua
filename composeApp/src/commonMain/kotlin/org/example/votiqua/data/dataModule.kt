package org.example.votiqua.data

import com.example.common.MyDispatchers
import org.example.votiqua.data.network.SearchRecommendRemoteDataSource
import org.example.votiqua.data.repository.AuthRepositoryImpl
import org.example.votiqua.data.repository.PollRepositoryImpl
import org.example.votiqua.data.repository.ProfileRepositoryImpl
import org.example.votiqua.data.repository.SearchRecommendRepository
import org.example.votiqua.data.repository.UserRepositoryImpl
import org.example.votiqua.domain.repository.AuthRepository
import org.example.votiqua.domain.repository.PollRepository
import org.example.votiqua.domain.repository.ProfileRepository
import org.example.votiqua.domain.repository.UserRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun dataModule() = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<PollRepository> { PollRepositoryImpl(get(named(MyDispatchers.IO))) }
    single<UserRepository> { UserRepositoryImpl() }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }

    single { SearchRecommendRepository(get()) }
    single { SearchRecommendRemoteDataSource(get()) }
}