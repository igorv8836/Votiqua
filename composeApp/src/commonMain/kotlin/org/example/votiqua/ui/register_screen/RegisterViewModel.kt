package org.example.votiqua.ui.register_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.launch
import org.example.votiqua.domain.repository.AuthRepository
import org.orbitmvi.orbit.ContainerHost

internal class RegisterViewModel(
    private val authRepository: AuthRepository
) : ContainerHost<RegisterState, RegisterEffect>, ViewModel() {
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
        if (result.isSuccess) {
            reduce { RegisterState(isLoading = false) }
            postSideEffect(RegisterEffect.NavigateToMain)
        } else {
            val exceptionMessage = result.exceptionOrNull()?.message ?: "Error"
            postSideEffect(RegisterEffect.ShowError(exceptionMessage))
            reduce { state.copy(isLoading = false) }
        }
    }
}

internal sealed interface RegisterEffect {
    data class ShowError(val message: String) : RegisterEffect
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