package org.example.votiqua.server.common.models

import kotlinx.serialization.Serializable

@Serializable
data class Parameter(val value: Int, val visibleToUser: Boolean, val text: String)

@Serializable
data object MainConfig {
    val MAX_LOGIN_ATTEMPTS: Parameter = Parameter(
        value = 15, visibleToUser = true, text = "Exceeded maximum login attempts (15). Wait before the next attempt"
    )
    val RESET_ATTEMPTS_INTERVAL_MINUTES: Parameter = Parameter(
        value = 60, visibleToUser = false, text = "The limit of login attempts has been reached, try again later"
    )
    val MIN_PASSWORD_LEN: Parameter = Parameter(
        value = 8,
        visibleToUser = true,
        text = "Password does not meet minimum length requirement (Minimum: 8 characters)"
    )
    val MAX_PASSWORD_LEN: Parameter = Parameter(
        value = 128, visibleToUser = true, text = "Password exceeds maximum allowed length (Maximum: 128 characters)"
    )

    val PASSWORD_RESET_TIMEOUT_MINUTES: Parameter =
        Parameter(value = 30, visibleToUser = true, text = "Password reset request has expired (Timeout: 30 minutes)")
    val EMAIL_VERIFICATION_TIMEOUT_MINUTES: Parameter = Parameter(
        value = 60, visibleToUser = true, text = "Email verification request has expired (Timeout: 60 minutes)"
    )
    val MAX_RESET_ATTEMPTS: Parameter = Parameter(
        value = 5,
        visibleToUser = true,
        text = "Exceeded maximum password reset attempts (5)."
    )

    val MIN_NICKNAME_LEN: Parameter = Parameter(
        value = 2,
        visibleToUser = true,
        text = "Nickname does not meet minimum length requirement (Minimum: 2 characters)"
    )
    val MAX_NICKNAME_LEN: Parameter = Parameter(
        value = 30, visibleToUser = true, text = "Nickname exceeds maximum allowed length (Maximum: 30 characters)"
    )

    val MAX_PHOTO_SIZE_MB: Parameter =
        Parameter(value = 5, visibleToUser = true, text = "Photo exceeds maximum allowed size (Maximum: 5 MB)")
}