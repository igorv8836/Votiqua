package com.example.feature.auth.ui.register_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.ResultExceptionHandler
import com.example.common.SnackbarManager
import com.example.feature.auth.data.repository.AuthRepository
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost

internal class RegisterViewModel(
    private val authRepository: AuthRepository,
    override val snackbarManager: SnackbarManager,
) : ContainerHost<RegisterState, RegisterEffect>, ViewModel(), ResultExceptionHandler {
    override val container = container<RegisterState, RegisterEffect>(RegisterState())

    fun onEvent(event: RegisterEvent) {
        viewModelScope.launch {
            when (event) {
                is RegisterEvent.Register -> {
                    register(event.email, event.password, event.nickname)
                }
            }
        }
    }

    private fun register(email: String, password: String, nickname: String) = intent {
        reduce { state.copy(isLoading = true) }
        val result = authRepository.signUp(email, password, nickname)

        result.onSuccess {
            reduce { RegisterState(isLoading = false) }
            postSideEffect(RegisterEffect.NavigateToMain)
        }.handleException {
            reduce { state.copy(isLoading = false) }
        }
    }
}

internal sealed interface RegisterEffect {
    data object NavigateToMain: RegisterEffect

}

internal sealed interface RegisterEvent {
    data class Register(val email: String, val password: String, val nickname: String) : RegisterEvent
}

@Stable
internal data class RegisterState(
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val nicknameError: String? = null,
)