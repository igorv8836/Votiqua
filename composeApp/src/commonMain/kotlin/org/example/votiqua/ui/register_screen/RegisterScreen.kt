package org.example.votiqua.ui.register_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orbit_mvi.compose.collectAsState
import com.example.orbit_mvi.compose.collectSideEffect
import org.example.votiqua.ui.common.PasswordOutlinedTextField
import org.example.votiqua.ui.navigation.navigateToMain
import org.koin.compose.viewmodel.koinViewModel


@Composable
internal fun RegisterScreen(
    navController: NavController,
    startEmail: String?,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect {
        when (it) {
            is RegisterEffect.ShowError -> {
                snackBarHostState.showSnackbar(it.message)
            }

            RegisterEffect.NavigateToMain -> {
                navController.navigateToMain()
            }
        }
    }

    RegisterScreen(
        popBackStack = navController::popBackStack,
        state = state,
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
    snackBarHostState: SnackbarHostState,
    onEvent: (RegisterEvent) -> Unit
) {
    var email by remember { mutableStateOf("") }
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