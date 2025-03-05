package org.example.votiqua.ui.splash_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.common.NetworkException
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.delay
import org.example.votiqua.domain.repository.AuthRepository
import org.orbitmvi.orbit.ContainerHost

internal class SplashViewModel(
    private val authRepository: AuthRepository
) : ContainerHost<SplashState, Nothing>, ViewModel() {
    override val container = container<SplashState, Nothing>(SplashState.Loading) {
        getUserInfo()
    }

    private fun getUserInfo() {
        intent {
            val result = authRepository.getUser()
            if (result.isSuccess) {
                val user = result.getOrThrow()
                if (!user.isActive) {
                    reduce { SplashState.Error("Ban: ${user.banReason ?: "-"}") }
                } else {
                    delay(500)
                    reduce { SplashState.Success }
                }
            } else {
                val exception = result.exceptionOrNull()
                if (exception is NetworkException.Unauthorized) {
                    reduce { SplashState.Unauthorized }
                } else {
                    reduce {
                        SplashState.Error(exception?.message ?: "Error")
                    }
                }
            }
        }
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.Retry -> {
                intent { reduce { SplashState.Loading } }
                getUserInfo()
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