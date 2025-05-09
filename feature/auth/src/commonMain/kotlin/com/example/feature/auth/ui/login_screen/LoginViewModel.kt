package com.example.feature.auth.ui.login_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.common.ResultExceptionHandler
import com.example.common.SnackbarManager
import com.example.feature.auth.data.repository.AuthRepository
import com.example.feature.auth.ui.common.AuthStringResources
import com.example.orbit_mvi.viewmodel.container
import org.orbitmvi.orbit.ContainerHost

internal class LoginViewModel(
    private val authRepository: AuthRepository,
    override val snackbarManager: SnackbarManager,
) : ContainerHost<LoginState, LoginEffect>, ViewModel(), ResultExceptionHandler {
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
                    snackbarManager.sendMessage(AuthStringResources.PASSWORD_CHANGED)
                    postSideEffect(LoginEffect.SuccessPasswordReset)
                }.handleException()
                reduce { state.copy(codeErrorText = null) }
            } ?: run {
                reduce { state.copy(codeErrorText = AuthStringResources.USE_DIGITS) }
            }
        }

    private fun login(email: String, password: String) = intent {
        reduce { state.copy(isLoading = true) }
        val result = authRepository.login(email, password)

        result.onSuccess {
            snackbarManager.sendMessage(AuthStringResources.SUCCESS_LOGIN)
            postSideEffect(LoginEffect.ShowSuccessLogin)
        }.handleException()

        reduce { state.copy(isLoading = false) }
    }

    private fun sendResetCode(email: String) = intent {
        val result = authRepository.sendResetCode(email)

        result.onSuccess {
            snackbarManager.sendMessage("Код отправлен")
        }.handleException()
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
    data object SuccessPasswordReset : LoginEffect
    data object ShowSuccessLogin : LoginEffect
}