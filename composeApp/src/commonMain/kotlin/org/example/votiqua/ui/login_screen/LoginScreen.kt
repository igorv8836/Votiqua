package org.example.votiqua.ui.login_screen

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import org.example.votiqua.ui.navigation.navigateToRegister
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import votiqua.composeapp.generated.resources.Res
import votiqua.composeapp.generated.resources.compose_multiplatform

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    navController: NavController
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is LoginEffect.ShowMessage -> {
                snackBarHostState.showSnackbar(it.message)
            }

            is LoginEffect.ShowSuccessLogin -> {
                snackBarHostState.showSnackbar(it.message)
                navController.navigateToMain()
            }

            is LoginEffect.ErrorInSendCode -> {
                snackBarHostState.showSnackbar(it.message)
            }
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState,
        navigateToRegister = { email ->
            navController.navigateToRegister(email)
        }
    )
}

@Composable
internal fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
    navigateToRegister: (String) -> Unit
) {
    var showRecoveryDialog by remember { mutableStateOf(false) }
    var emailField by rememberSaveable { mutableStateOf("") }
    var passwordField by rememberSaveable { mutableStateOf("") }

    if (showRecoveryDialog) {
        RecoveryDialog(state, onEvent){
            showRecoveryDialog = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it).verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(resource = Res.drawable.compose_multiplatform),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .size(240.dp)
                )


                OutlinedTextField(
                    value = emailField,
                    supportingText = {
                        if (state.emailErrorText != null){
                            Text(state.emailErrorText)
                        }
                    },
                    isError = state.emailErrorText != null,
                    onValueChange = { it1 -> emailField = it1 },
                    label = { Text("Введите почту") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "email")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp)
                )

                PasswordOutlinedTextField(
                    text = passwordField,
                    passwordErrorText = state.passwordErrorText ?: "",
                    modifier = Modifier.fillMaxWidth()
                ){ it1 ->
                    passwordField = it1
                }

                TextButton(
                    onClick = { showRecoveryDialog = true },
                ) {
                    Text("Забыли пароль?")
                }

                Spacer(modifier = Modifier.height(4.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        enabled = !state.isLoading,
                        onClick = { onEvent(LoginEvent.Login(emailField, passwordField)) },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .padding(bottom = 8.dp)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator()
                        } else {
                            Text("Войти")
                        }
                    }

                    Button(
                        onClick = { navigateToRegister(emailField) },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth(0.75f)
                    ) {
                        Text("Зарегистрироваться")
                    }
                }

            }
        }

    }
}

@Composable
internal fun RecoveryDialog(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    dismiss: () -> Unit
) {
    var emailField by rememberSaveable{ mutableStateOf("") }
    var codeField by rememberSaveable{ mutableStateOf("") }
    var newPasswordField by rememberSaveable{ mutableStateOf("") }
    var showAdditionMenu by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        title = { Text("Восстановление пароля") },
        onDismissRequest = dismiss,
        confirmButton = {
            if (emailField.isNotBlank()) {
                TextButton(onClick = {
                    if (showAdditionMenu){
                        onEvent(LoginEvent.UseResetCode(emailField, codeField, newPasswordField))
                    } else {
                        showAdditionMenu = true
                        onEvent(LoginEvent.SendResetCode(emailField))
                    }
                }) {
                    Text("Отправить")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text("Отмена")
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    enabled = !showAdditionMenu,
                    supportingText = {
                        if (state.resetEmailErrorText != null){
                            Text(state.resetEmailErrorText)
                        }
                    },
                    isError = state.resetEmailErrorText != null,
                    value = emailField,
                    onValueChange = { emailField = it },
                    label = { Text("Введите почту") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    onClick = { showAdditionMenu = !showAdditionMenu },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Ввести код и новый пароль")
                }

                AnimatedVisibility(
                    visible = showAdditionMenu,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        OutlinedTextField(
                            value = codeField,
                            supportingText = {
                                if (state.codeErrorText != null){
                                    Text(state.codeErrorText)
                                }
                            },
                            isError = state.codeErrorText != null,
                            onValueChange = { codeField = it },
                            label = { Text("Введите код") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )

                        PasswordOutlinedTextField(
                            text = newPasswordField,
                            passwordErrorText = state.newPasswordErrorText ?: "",
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            label = "Введите новый пароль"
                        ){ it1 ->
                            newPasswordField = it1
                        }
                    }
                }
            }
        }
    )
}