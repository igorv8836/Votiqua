package org.example.votiqua.server.feature.recom.di

import org.example.votiqua.server.feature.recom.data.repository.RecomRepository
import org.koin.dsl.module

fun recomModule() = module {
    single { RecomRepository(get()) }
} 