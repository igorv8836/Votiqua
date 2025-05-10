package com.example.feature.profile.ui.profile_screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feature.profile.ui.profile_screen.elements.ChangePasswordDialog
import com.example.feature.profile.ui.profile_screen.elements.ProfileAdditionalDialog
import com.example.feature.profile.ui.profile_screen.elements.SectionTitle
import com.example.feature.profile.ui.profile_screen.elements.SettingsItem
import com.example.orbit_mvi.compose.collectAsState
import com.example.orbit_mvi.compose.collectSideEffect
import com.example.votiqua.core.ui_common.constants.Dimens
import com.example.votiqua.core.ui_common.navigation.navigateToFavourite
import com.example.votiqua.core.ui_common.navigation.navigateToLogin
import org.jetbrains.compose.resources.stringResource
import votiqua.core.ui_common.generated.resources.Res
import votiqua.core.ui_common.generated.resources.dark_theme
import votiqua.core.ui_common.generated.resources.disabled
import votiqua.core.ui_common.generated.resources.enabled
import votiqua.core.ui_common.generated.resources.system

@Composable
internal fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController,
) {
    val state by viewModel.collectAsState()

    val scrollState = rememberScrollState()

    viewModel.collectSideEffect {
        when (it) {
            ProfileSideEffect.SignedOut -> navController.navigateToLogin()
        }
    }

    if (state.showNicknameDialog) {
        ProfileAdditionalDialog(
            title = "Изменение никнейма",
            textFieldLabel = "Введите новый никнейм",
            denyRequest = { viewModel.onEvent(ProfileEvent.SetShowNicknameDialog(false)) },
        ) {
            viewModel.onEvent(it)
        }
    }

    if (state.showPasswordDialog) {
        ChangePasswordDialog(
            denyRequest = { viewModel.onEvent(ProfileEvent.SetShowPasswordDialog(false)) },
        ) {
            viewModel.onEvent(it)
        }
    }

    Column(
        modifier = Modifier
            .padding(top = Dimens.small)
            .fillMaxHeight()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = Dimens.medium),
            contentAlignment = Alignment.TopCenter
        ) {
            ProfilePhoto(state.photoFile, viewModel)
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
                    viewModel.onEvent(ProfileEvent.SetShowNicknameDialog(true))
                }
                SettingsItem("Изменить пароль") {
                    viewModel.onEvent(ProfileEvent.SetShowPasswordDialog(true))
                }
                SettingsItem(
                    "Выйти с аккаунта",
                    isLast = true,
                    textColor = Color.Red,
                ) {
                    viewModel.onEvent(ProfileEvent.SignOut)
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
    viewModel: ProfileViewModel,
)