package org.example.votiqua.ui

import org.example.votiqua.ui.login_screen.LoginViewModel
import org.example.votiqua.ui.register_screen.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun uiModule() = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}