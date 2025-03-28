package org.example.votiqua.ui.profile_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.ThemeMode
import com.example.common.toThemeMode
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.launch
import org.example.votiqua.domain.repository.AuthRepository
import org.example.votiqua.domain.repository.ProfileRepository
import org.example.votiqua.domain.repository.UserRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

internal class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
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
        intent {
            reduce {
                val user = userRepository.getUser()
                state.copy(
                    email = user.email,
                    nickname = user.nickname,
                    photoFile = user.photoUrl,
                )
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.SignOut -> signOut()
            ProfileEvent.ChangeThemeMode -> changeThemeMode()

            is ProfileEvent.ChangeNickname -> changeNickname(
                newNickname = event.newNickname
            )

            is ProfileEvent.ChangePassword -> changePassword(
                last = event.last,
                new = event.new
            )

            is ProfileEvent.ChangePhoto -> changePhoto(
                uri = event.uri
            )
        }
    }

    private fun changeNickname(newNickname: String) = intent {
        try {
            userRepository.updateNickname(newNickname)
            reduce {
                state.copy(
                    nickname = newNickname,
                    helpingText = "Никнейм успешно изменён"
                )
            }
        } catch (e: Exception) {
            reduce {
                state.copy(helpingText = e.message ?: "Ошибка изменения никнейма")
            }
        }
    }

    private fun changePassword(last: String, new: String) = intent {
        try {
            authRepository.changePassword(state.email, last, new)
            reduce { state.copy(helpingText = "Пароль успешно изменён") }
        } catch (e: Exception) {
            reduce { state.copy(helpingText = e.message ?: "Ошибка изменения пароля") }
        }
    }

    private fun changePhoto(uri: String) = intent {
        try {
            userRepository.updatePhoto(uri)
            reduce { state.copy(photoFile = uri, helpingText = "Фото успешно обновлено") }
        } catch (e: Exception) {
            reduce { state.copy(helpingText = e.message ?: "Ошибка обновления фото") }
        }
    }

    private fun signOut() = intent {
        postSideEffect(ProfileSideEffect.SignedOut)
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
    val helpingText: String = "",
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
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
}