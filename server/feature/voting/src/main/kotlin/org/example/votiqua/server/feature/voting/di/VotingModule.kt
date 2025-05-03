package org.example.votiqua.server.feature.voting.di

import org.example.votiqua.server.feature.voting.data.repository.PollFavoriteRepository
import org.example.votiqua.server.feature.voting.data.repository.PollOptionRepository
import org.example.votiqua.server.feature.voting.data.repository.PollRepository
import org.example.votiqua.server.feature.voting.data.repository.TagRepository
import org.example.votiqua.server.feature.voting.domain.usecase.FavoritePollUseCase
import org.example.votiqua.server.feature.voting.domain.usecase.PollUpdateUseCase
import org.example.votiqua.server.feature.voting.domain.usecase.PollUseCase
import org.koin.dsl.module

fun votingModule() = module {
    single<PollRepository> { PollRepository(get(), get()) }
    single<TagRepository> { TagRepository() }
    single<PollOptionRepository> { PollOptionRepository() }
    single<PollFavoriteRepository> { PollFavoriteRepository() }
    
    single { PollUseCase(get(), get()) }
    single { FavoritePollUseCase(get(), get()) }
    single { PollUpdateUseCase(get()) }
}