package com.example.feature.auth.ui.splash_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.feature.auth.data.repository.AuthRepository
import com.example.orbit_mvi.viewmodel.container
import org.orbitmvi.orbit.ContainerHost

internal class SplashViewModel(
    private val authRepository: AuthRepository
) : ContainerHost<SplashState, Nothing>, ViewModel() {
    override val container = container<SplashState, Nothing>(SplashState.Loading) {
        checkToken()
    }

    private fun checkToken() = intent {
        val result = authRepository.checkToken()

        result.onSuccess { isValidToken ->
            reduce {
                if (isValidToken) {
                    SplashState.Success
                } else {
                    SplashState.Unauthorized
                }
            }
        }.onFailure {
            reduce { SplashState.Error(it.message ?: "Error") }
        }
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.Retry -> {
                intent { reduce { SplashState.Loading } }
                checkToken()
            }
        }
    }
}

internal sealed interface SplashEvent {
    data object Retry : SplashEvent
}

@Stable
internal sealed interface SplashState {
    data object Loading : SplashState
    data object Success : SplashState
    data object Unauthorized : SplashState
    data class Error(val message: String) : SplashState
}