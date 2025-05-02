package org.example.votiqua.ui

import org.example.votiqua.ui.manage_poll_screen.ManagePollViewModel
import org.example.votiqua.ui.poll_viewer_screen.PollViewerViewModel
import org.example.votiqua.ui.search_screen.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun uiModule() = module {
    viewModel { ManagePollViewModel(get()) }
    viewModel { PollViewerViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { AppViewModel(get()) }
}