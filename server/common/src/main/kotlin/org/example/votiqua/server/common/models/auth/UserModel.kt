package org.example.votiqua.server.common.models.auth

import kotlinx.serialization.Serializable
import org.example.votiqua.models.auth.User
import org.example.votiqua.server.common.utils.currentTimestamp

@Serializable
data class UserModel(
    override val id: Int,
    val email: String,
    val passwordHash: String,
    override val username: String,
    override val photoUrl: String?,
    val notificationEnabled: Boolean,
    val isActive: Boolean,
    val banReason: String?,
    val createdAt: Long,
    override val isAdmin: Boolean = false
) : User {

    constructor(
        email: String,
        passwordHash: String,
        username: String,
    ) : this(
        id = 0,
        email = email,
        passwordHash = passwordHash,
        username = username,
        photoUrl = null,
        notificationEnabled = true,
        isActive = true,
        banReason = null,
        createdAt = currentTimestamp()
    )
}