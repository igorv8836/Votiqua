package org.example.votiqua.server.feature.voting.di

import org.example.votiqua.server.feature.voting.data.repository.PollFavoriteRepository
import org.example.votiqua.server.feature.voting.data.repository.PollOptionRepository
import org.example.votiqua.server.feature.voting.data.repository.PollParticipantRepository
import org.example.votiqua.server.feature.voting.data.repository.PollRepository
import org.example.votiqua.server.feature.voting.data.repository.TagRepository
import org.example.votiqua.server.feature.voting.domain.usecase.FavoritePollUseCase
import org.example.votiqua.server.feature.voting.domain.usecase.GetPollUseCase
import org.example.votiqua.server.feature.voting.domain.usecase.PollManageUseCase
import org.example.votiqua.server.feature.voting.domain.usecase.PollMemberUseCase
import org.example.votiqua.server.feature.voting.domain.usecase.PollUpdateUseCase
import org.koin.dsl.module

fun votingModule() = module {
    single<PollRepository> { PollRepository(get(), get(), get()) }
    single<TagRepository> { TagRepository() }
    single<PollOptionRepository> { PollOptionRepository() }
    single<PollFavoriteRepository> { PollFavoriteRepository() }
    single<PollParticipantRepository> { PollParticipantRepository(get()) }

    single { GetPollUseCase(get(), lazy { get() }) }
    
    single { PollManageUseCase(get(), get()) }
    single { FavoritePollUseCase(get(), get()) }
    single { PollUpdateUseCase(get(), get(), get()) }
    single { PollMemberUseCase(get(), get(), get()) }
}