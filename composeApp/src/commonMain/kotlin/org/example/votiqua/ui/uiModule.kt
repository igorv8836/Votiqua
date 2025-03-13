package org.example.votiqua.ui

import org.example.votiqua.ui.login_screen.LoginViewModel
import org.example.votiqua.ui.manage_poll_screen.ManagePollViewModel
import org.example.votiqua.ui.poll_viewer_screen.PollViewerViewModel
import org.example.votiqua.ui.profile_screen.ProfileViewModel
import org.example.votiqua.ui.register_screen.RegisterViewModel
import org.example.votiqua.ui.splash_screen.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun uiModule() = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { ManagePollViewModel(get()) }
    viewModel { PollViewerViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
}