package com.example.feature.voting

import com.example.feature.voting.data.PollMemberRemoteDataSource
import com.example.feature.voting.data.PollRemoteDataSource
import com.example.feature.voting.data.repository.PollRepository
import com.example.feature.voting.data.repository.PollRepositoryImpl
import com.example.feature.voting.domain.PollMapper
import com.example.feature.voting.navigation.VotingNavigator
import com.example.feature.voting.navigation.VotingNavigatorImpl
import com.example.feature.voting.ui.manage_poll_screen.ManagePollViewModel
import com.example.feature.voting.ui.poll_list_screen.PollListViewModel
import com.example.feature.voting.ui.poll_viewer_screen.PollViewerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun votingModule() = module {
    single<VotingNavigator> { VotingNavigatorImpl() }

    single<PollRepository> { PollRepositoryImpl(get(), get(), get()) }
    single<PollRemoteDataSource> { PollRemoteDataSource(get()) }
    single<PollMemberRemoteDataSource> { PollMemberRemoteDataSource(get()) }

    factory<PollMapper> { PollMapper() }

    viewModel { (pollId: Int?) -> ManagePollViewModel(pollId, get(), get(), get()) }
    viewModel { (pollId: Int) -> PollViewerViewModel(pollId, get(), get(), get()) }
    viewModel { PollListViewModel(get(), get()) }
}