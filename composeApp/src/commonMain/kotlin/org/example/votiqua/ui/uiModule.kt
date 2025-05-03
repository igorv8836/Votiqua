package org.example.votiqua.ui

import org.example.votiqua.ui.search_screen.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun uiModule() = module {
    viewModel { SearchViewModel(get()) }
    viewModel { AppViewModel(get()) }
}