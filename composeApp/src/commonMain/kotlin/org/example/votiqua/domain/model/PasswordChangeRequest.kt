package org.example.votiqua.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeRequest(
    val email: String,
    val oldPassword: String,
    val newPassword: String
)