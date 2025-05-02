package org.example.votiqua.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.ThemeMode
import com.example.feature.profile.api.data.repository.ProfileRepository
import com.example.orbit_mvi.viewmodel.container
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

internal class AppViewModel(
    private val profileRepository: ProfileRepository,
) : ContainerHost<AppState, Nothing>, ViewModel() {
    override val container: Container<AppState, Nothing> = container(AppState())

    init {
        viewModelScope.launch {
            profileRepository.getThemeMode().collect { mode ->
                intent {
                    reduce { state.copy(themeMode = mode) }
                }
            }
        }
    }
}

@Stable
data class AppState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)