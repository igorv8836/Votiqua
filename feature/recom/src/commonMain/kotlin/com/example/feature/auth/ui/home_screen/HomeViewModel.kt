package com.example.feature.auth.ui.home_screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.example.common.ResultExceptionHandler
import com.example.common.SnackbarManager
import com.example.feature.voting.domain.models.PollCardState
import com.example.orbit_mvi.viewmodel.container
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

@Stable
data class HomeState(
    val messages: List<String> = emptyList(),
    val popularPolls: List<PollCardState> = emptyList(),
    val newPolls: List<PollCardState> = emptyList(),
)

sealed interface HomeEffect {

}

class HomeViewModel(
    override val snackbarManager: SnackbarManager,
) : ViewModel(), ContainerHost<HomeState, HomeEffect>, ResultExceptionHandler {
    override val container: Container<HomeState, HomeEffect> = container(HomeState())
}