package com.example.feature.auth.ui.login_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.feature.auth.data.repository.AuthRepository
import com.example.feature.auth.ui.common.AuthStringResources
import com.example.orbit_mvi.viewmodel.container
import org.orbitmvi.orbit.ContainerHost

internal class LoginViewModel(
    private val authRepository: AuthRepository
) : ContainerHost<LoginState, LoginEffect>, ViewModel() {
    override val container = container<LoginState, LoginEffect>(LoginState())

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> login(event.email, event.password)
            is LoginEvent.SendResetCode -> sendResetCode(event.email)
            is LoginEvent.UseResetCode -> useResetCode(event.email, event.code, event.newPassword)
        }
    }

    private fun useResetCode(email: String, code: String, newPassword: String) =
        intent {
            code.toIntOrNull()?.let {
                val result = authRepository.resetPassword(
                    email = email,
                    resetCode = it,
                    newPassword = newPassword,
                )

                result.onSuccess {
                    postSideEffect(LoginEffect.SuccessPasswordReset(AuthStringResources.PASSWORD_CHANGED))
                }.onFailure {
                    val exceptionMessage = result.exceptionOrNull()?.message
                    postSideEffect(LoginEffect.ShowMessage(exceptionMessage))
                }
                reduce { state.copy(codeErrorText = null) }
            } ?: run {
                reduce { state.copy(codeErrorText = AuthStringResources.USE_DIGITS) }
            }
        }

    private fun login(email: String, password: String) = intent {
        reduce { state.copy(isLoading = true) }
        val result = authRepository.login(email, password)

        result.onSuccess {
            postSideEffect(LoginEffect.ShowSuccessLogin(AuthStringResources.SUCCESS_LOGIN))
        }.onFailure {
            val exceptionMessage = it.message
            postSideEffect(LoginEffect.ShowMessage(exceptionMessage))
        }

        reduce { state.copy(isLoading = false) }
    }

    private fun sendResetCode(email: String) = intent {
        val result = authRepository.sendResetCode(email)

        result.onSuccess {
            postSideEffect(LoginEffect.ShowMessage("Код отправлен"))
        }.onFailure {
            val exceptionMessage = result.exceptionOrNull()?.message
            postSideEffect(LoginEffect.ErrorInSendCode(exceptionMessage))
        }
    }
}

@Stable
sealed interface LoginEvent {
    data class Login(val email: String, val password: String) : LoginEvent
    data class SendResetCode(val email: String) : LoginEvent
    data class UseResetCode(val email: String, val code: String, val newPassword: String) : LoginEvent
}


@Stable
data class LoginState(
    val isLoading: Boolean = false,
    val emailErrorText: String? = null,
    val passwordErrorText: String? = null,
    val resetEmailErrorText: String? = null,
    val newPasswordErrorText: String? = null,
    val codeErrorText: String? = null,
)


@Stable
internal sealed interface LoginEffect {
    data class ShowMessage(val message: String?) : LoginEffect
    data class SuccessPasswordReset(val message: String?) : LoginEffect
    data class ErrorInSendCode(val message: String?) : LoginEffect
    data class ShowSuccessLogin(val message: String) : LoginEffect
}