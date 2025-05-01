package com.example.feature.auth.ui.register_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orbit_mvi.compose.collectAsState
import com.example.orbit_mvi.compose.collectSideEffect
import com.example.votiqua.core.ui_common.components.text_fields.PasswordOutlinedTextField
import com.example.votiqua.core.ui_common.navigation.navigateToMain
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import votiqua.core.ui_common.generated.resources.Res
import votiqua.core.ui_common.generated.resources.basic_error


@Composable
internal fun RegisterScreen(
    navController: NavController,
    startEmail: String?,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val basicErrorText = stringResource(Res.string.basic_error)

    viewModel.collectSideEffect {
        when (it) {
            is RegisterEffect.ShowError -> {
                snackBarHostState.showSnackbar(it.message ?: basicErrorText)
            }

            RegisterEffect.NavigateToMain -> {
                navController.navigateToMain()
            }
        }
    }

    RegisterScreen(
        popBackStack = navController::popBackStack,
        state = state,
        startEmail = startEmail ?: "",
        snackBarHostState = snackBarHostState
    ) {
        viewModel.onEvent(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RegisterScreen(
    popBackStack: () -> Unit,
    state: RegisterState,
    startEmail: String,
    snackBarHostState: SnackbarHostState,
    onEvent: (RegisterEvent) -> Unit
) {
    var email by remember { mutableStateOf(startEmail) }
    var password by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Регистрация") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "arrowBackButton"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp).padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                isError = state.emailError != null,
                supportingText = {
                    if (state.emailError != null){
                        Text(state.emailError)
                    }
                },
                label = { Text("Почта") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordOutlinedTextField(
                text = password,
                passwordErrorText = state.passwordError ?: "",
                modifier = Modifier.fillMaxWidth()
            ){ it1 ->
                password = it1
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("Никнейм") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isError = state.nicknameError != null,
                supportingText = {
                    if (state.nicknameError != null){
                        Text(state.nicknameError)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onEvent(RegisterEvent.Register(email, password, nickname))
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Зарегистрироваться")
                }
            }
        }
    }
}