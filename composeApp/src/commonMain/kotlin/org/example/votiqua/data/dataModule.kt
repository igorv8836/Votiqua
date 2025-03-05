package org.example.votiqua.data

import org.example.votiqua.data.repository.AuthRepositoryImpl
import org.example.votiqua.domain.repository.AuthRepository
import org.koin.dsl.module

fun dataModule() = module {
    single<AuthRepository> { AuthRepositoryImpl() }
}