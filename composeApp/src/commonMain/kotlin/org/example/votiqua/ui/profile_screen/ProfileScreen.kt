package org.example.votiqua.ui.profile_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orbit_mvi.compose.collectAsState
import org.example.votiqua.ui.common.Dimens
import org.example.votiqua.ui.navigation.navigateToFavourite
import org.example.votiqua.ui.navigation.navigateToLogin
import org.example.votiqua.ui.profile_screen.elements.ChangePasswordDialog
import org.example.votiqua.ui.profile_screen.elements.ProfileAdditionalDialog
import org.example.votiqua.ui.profile_screen.elements.SectionTitle
import org.example.votiqua.ui.profile_screen.elements.SettingsItem

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
            viewModel.changeNickname(it)
            showNicknameDialog = false
        }
    }

    if (showPasswordDialog) {
        ChangePasswordDialog(
            denyRequest = { showPasswordDialog = false }) { last, new ->
            viewModel.changePassword(last, new)
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
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.small)
            )
            Text(
                text = state.email,
                style = MaterialTheme.typography.bodyLarge,
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
                    Switcher()
                }

            }
        }
    }
}

@Composable
fun Switcher() {
    var isSwitchEnabled by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.medium, bottom = Dimens.medium, start = Dimens.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Темный режим",
            style = MaterialTheme.typography.titleMedium
        )
        androidx.compose.material3.Switch(
            checked = isSwitchEnabled,
            onCheckedChange = { isSwitchEnabled = it }
        )
    }
}

@Composable
expect fun ProfilePhoto(
    url: String?,
)