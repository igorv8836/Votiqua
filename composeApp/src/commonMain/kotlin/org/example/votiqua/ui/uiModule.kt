package org.example.votiqua.ui

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun uiModule() = module {
    viewModel { AppViewModel(get()) }
}