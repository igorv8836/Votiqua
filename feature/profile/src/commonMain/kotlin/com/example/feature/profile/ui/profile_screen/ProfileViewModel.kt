package com.example.feature.profile.ui.profile_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.SnackbarManager
import com.example.common.ThemeMode
import com.example.common.toThemeMode
import com.example.feature.auth.data.repository.AuthRepository
import com.example.feature.profile.api.data.repository.ProfileRepository
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

internal class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val snackbarManager: SnackbarManager,
) : ContainerHost<ProfileState, ProfileSideEffect>, ViewModel() {

    override val container: Container<ProfileState, ProfileSideEffect> = container(ProfileState())

    init {
        viewModelScope.launch {
            profileRepository.getThemeMode().collect { mode ->
                intent {
                    reduce { state.copy(themeMode = mode) }
                }
            }
        }
        loadUserProfile()
    }

    private fun loadUserProfile() = intent {
        val result = profileRepository.getUserProfile()

        result.onSuccess { user ->
            reduce {
                state.copy(
                    email = user.email,
                    nickname = user.nickname,
                    photoFile = user.photoUrl,
                )
            }
        }.onFailure { e ->
            snackbarManager.sendMessage("Ошибка загрузки профиля: ${e.message}")
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.SignOut -> signOut()
            ProfileEvent.ChangeThemeMode -> changeThemeMode()
            is ProfileEvent.ChangeNickname -> changeNickname(event.newNickname)
            is ProfileEvent.ChangePassword -> changePassword(event.last, event.new)
            is ProfileEvent.ChangePhoto -> changePhoto(event.uri)
            is ProfileEvent.SetShowNicknameDialog -> intent { reduce { state.copy(showNicknameDialog = event.value) } }
            is ProfileEvent.SetShowPasswordDialog -> intent { reduce { state.copy(showPasswordDialog = event.value) } }
        }
    }

    private fun changeNickname(newNickname: String) = intent {
        val result = profileRepository.updateUserProfile(
            username = newNickname,
            description = null, //  TODO
        )

        result.onSuccess { updatedUser ->
            reduce {
                state.copy(
                    nickname = updatedUser.nickname,
                )
            }
            snackbarManager.sendMessage("Никнейм успешно изменён")
        }.onFailure {
            snackbarManager.sendMessage(it.message ?: "Ошибка изменения никнейма")
        }

        reduce {
            state.copy(showNicknameDialog = false)
        }
    }

    private fun changePassword(last: String, new: String) = intent {
        val result = authRepository.changePassword(
            oldPassword = last,
            newPassword = new
        )

        result.onSuccess {
            snackbarManager.sendMessage("Пароль успешно изменён")
        }.onFailure {
            snackbarManager.sendMessage(it.message ?: "Ошибка изменения пароля")
        }

        reduce { state.copy(showPasswordDialog = false) }
    }

    private fun changePhoto(uri: String) = intent {
        try {
//            val file = File(uri) //  TODO
//            val fileExtension = file.extension
//            val updatedUser = profileRepository.updateUserPhoto(
//                file.inputStream().asInput(),
//                fileExtension
//            ).getOrThrow()
//            reduce {
//                state.copy(
//                    photoFile = updatedUser.photoUrl,
//                    helpingText = "Фото успешно обновлено"
//                )
//            }
        } catch (e: Exception) {
            snackbarManager.sendMessage(e.message ?: "Ошибка обновления фото")
        }
    }

    private fun signOut() = intent {
        authRepository.logout().onSuccess {
            postSideEffect(ProfileSideEffect.SignedOut)
        }.onFailure {
            snackbarManager.sendMessage(message = null)
        }
    }

    private fun changeThemeMode() = intent {
        profileRepository.setThemeMode(((state.themeMode.value % 3) + 1).toThemeMode())
    }
}

@Stable
data class ProfileState(
    val email: String = "",
    val nickname: String = "",
    val photoFile: String? = null,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val showNicknameDialog: Boolean = false,
    val showPasswordDialog: Boolean = false,
)

sealed interface ProfileSideEffect {
    object SignedOut : ProfileSideEffect
}

sealed interface ProfileEvent {
    data class ChangeNickname(val newNickname: String) : ProfileEvent
    data class ChangePassword(val last: String, val new: String) : ProfileEvent
    data class ChangePhoto(val uri: String) : ProfileEvent
    object SignOut : ProfileEvent
    object ChangeThemeMode: ProfileEvent
    data class SetShowNicknameDialog(val value: Boolean): ProfileEvent
    data class SetShowPasswordDialog(val value: Boolean): ProfileEvent
}