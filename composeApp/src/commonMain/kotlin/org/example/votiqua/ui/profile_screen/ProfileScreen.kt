package org.example.votiqua.ui.profile_screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.common.toThemeMode
import com.example.orbit_mvi.compose.collectAsState
import org.example.votiqua.ui.common.Dimens
import org.example.votiqua.ui.navigation.navigateToFavourite
import org.example.votiqua.ui.navigation.navigateToLogin
import org.example.votiqua.ui.profile_screen.elements.ChangePasswordDialog
import org.example.votiqua.ui.profile_screen.elements.ProfileAdditionalDialog
import org.example.votiqua.ui.profile_screen.elements.SectionTitle
import org.example.votiqua.ui.profile_screen.elements.SettingsItem
import org.jetbrains.compose.resources.stringResource
import votiqua.composeapp.generated.resources.Res
import votiqua.composeapp.generated.resources.dark_theme
import votiqua.composeapp.generated.resources.disabled
import votiqua.composeapp.generated.resources.enabled
import votiqua.composeapp.generated.resources.loading
import votiqua.composeapp.generated.resources.system

@Composable
internal fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController,
) {
    val state by viewModel.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var showNicknameDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    if (showNicknameDialog) {
        ProfileAdditionalDialog(
            title = "Изменение никнейма",
            textFieldLabel = "Введите новый никнейм",
            denyRequest = { showNicknameDialog = false }) {
            viewModel::onEvent
            showNicknameDialog = false
        }
    }

    if (showPasswordDialog) {
        ChangePasswordDialog(
            denyRequest = { showPasswordDialog = false }) {
            viewModel::onEvent
            showPasswordDialog = false
        }
    }

    LaunchedEffect(state.helpingText) {
        if (state.helpingText.isNotEmpty())
            snackbarHostState.showSnackbar(state.helpingText)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.padding(top = Dimens.small)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = Dimens.medium),
                contentAlignment = Alignment.TopCenter
            ) {
                ProfilePhoto(state.photoFile)
            }

            Text(
                text = state.nickname,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.small)
            )
            Text(
                text = state.email,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Surface(
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = Dimens.small)
                    .padding(top = Dimens.large)
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.small)
                ) {
                    SectionTitle(title = "Настройки аккаунта")
                    SettingsItem("Избранное") {
                        navController.navigateToFavourite()
                    }
                    SettingsItem("Изменить никнейм") {
                        showNicknameDialog = true
                    }
                    SettingsItem("Изменить пароль") {
                        showPasswordDialog = true
                    }
                    SettingsItem(
                        "Выйти с аккаунта",
                        isLast = true,
                        textColor = Color.Red,
                    ) {
                        navController.navigateToLogin()
                    }
                    SectionTitle(title = "Настройки приложения")
                    ThreeStateSwitcher(
                        state = state.themeMode.value,
                        onEvent = viewModel::onEvent,
                    )
                }

            }
        }
    }
}

@Composable
fun ThreeStateSwitcher(
    state: Int,
    onEvent: (ProfileEvent) -> Unit,
) {
    val segmentWidth = 30.dp
    val totalWidth = segmentWidth * 3
    val thumbOffset by animateDpAsState(
        targetValue = segmentWidth * (state - 1),
        animationSpec = tween(durationMillis = 250)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.medium, bottom = Dimens.medium, start = Dimens.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.dark_theme) + when (state) {
                1 -> stringResource(Res.string.disabled)
                2 -> stringResource(Res.string.enabled)
                3 -> stringResource(Res.string.system)
                else -> ""
            },
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),

        )
        Box(
            modifier = Modifier
                .width(totalWidth)
                .height(segmentWidth)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .clickable {
                    onEvent(ProfileEvent.ChangeThemeMode)
                }
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset)
                    .size(segmentWidth)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}

@Composable
expect fun ProfilePhoto(
    url: String?,
)