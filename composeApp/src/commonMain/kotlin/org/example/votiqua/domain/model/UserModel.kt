package org.example.votiqua.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val email: String,
    val username: String,
    val photoUrl: String?,
    val notificationEnabled: Boolean,
    val isActive: Boolean,
    val banReason: String?
)
