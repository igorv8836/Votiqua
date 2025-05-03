package org.example.votiqua.di

import org.example.votiqua.presentation.polls.favorites.FavoritePollsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun pollModule(): Module = module {
    viewModel { FavoritePollsViewModel(get()) }
} 