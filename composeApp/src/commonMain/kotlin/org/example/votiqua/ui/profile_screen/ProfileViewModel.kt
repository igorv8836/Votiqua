package org.example.votiqua.ui.profile_screen

import androidx.lifecycle.ViewModel
import com.example.orbit_mvi.viewmodel.container
import org.example.votiqua.domain.repository.AuthRepository
import org.example.votiqua.domain.repository.UserRepository
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

internal class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ContainerHost<ProfileState, ProfileSideEffect>, ViewModel() {

    override val container: Container<ProfileState, ProfileSideEffect> = container(ProfileState())

    init {
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

    fun changeNickname(newNickname: String) = intent {
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

    fun changePassword(last: String, new: String) = intent {
        try {
            authRepository.changePassword(state.email, last, new)
            reduce { state.copy(helpingText = "Пароль успешно изменён") }
        } catch (e: Exception) {
            reduce { state.copy(helpingText = e.message ?: "Ошибка изменения пароля") }
        }
    }

    fun changePhoto(uri: String) = intent {
        try {
            userRepository.updatePhoto(uri)
            reduce { state.copy(photoFile = uri, helpingText = "Фото успешно обновлено") }
        } catch (e: Exception) {
            reduce { state.copy(helpingText = e.message ?: "Ошибка обновления фото") }
        }
    }

    fun signOut() = intent {
//        authRepository.signUp()
        postSideEffect(ProfileSideEffect.SignedOut)
    }
}

data class ProfileState(
    val email: String = "",
    val nickname: String = "",
    val photoFile: String? = null,
    val helpingText: String = ""
)

sealed interface ProfileSideEffect {
    object SignedOut : ProfileSideEffect
}