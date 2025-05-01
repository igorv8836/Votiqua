package com.example.votiqua.core.ui_common.components.text_fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PasswordOutlinedTextField(
    text: String,
    passwordErrorText: String,
    modifier: Modifier,
    label: String = "Введите пароль",
    onValueChanged: (String) -> Unit
    ) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = text,
        supportingText = {
            if (passwordErrorText.isNotBlank()){
                Text(passwordErrorText)
            }
        },
        isError = passwordErrorText.isNotBlank(),
        onValueChange = onValueChanged,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            val image =
                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        leadingIcon = {
            Icon(Icons.Default.Password, contentDescription = "password")
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
    )
}

@Composable
@Preview
internal fun PasswordOutlinedTextFieldPreview() {
    PasswordOutlinedTextField(
        text = "password",
        passwordErrorText = "Error",
        modifier = Modifier,
        label = "Введите пароль",
    ) {
    }
}
