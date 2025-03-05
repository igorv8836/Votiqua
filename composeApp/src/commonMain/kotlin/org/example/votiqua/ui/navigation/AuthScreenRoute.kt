package org.example.votiqua.ui.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

@Serializable
data object SplashRoute

@Serializable
data class RegisterRoute(val email: String? = null)

fun NavController.navigateToLogin() = navigate(LoginRoute)
fun NavController.navigateToRegister(email: String?) = navigate(RegisterRoute(email))