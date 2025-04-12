package org.example.votiqua.server.feature.voting.di

import org.example.votiqua.server.feature.voting.data.repository.PollAuthorRepository
import org.example.votiqua.server.feature.voting.data.repository.PollRepository
import org.example.votiqua.server.feature.voting.domain.usecase.PollUseCase
import org.koin.dsl.module

fun votingModule() = module {
    single { PollAuthorRepository() }
    single { PollRepository() }


    single { PollUseCase(get(), get()) }
}