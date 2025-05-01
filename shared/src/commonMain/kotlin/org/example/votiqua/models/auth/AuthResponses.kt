package org.example.votiqua.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)

@Serializable
data class BaseResponse(
    val message: String
)

@Serializable
data class UsernameCheckResponse(
    val isUsed: Boolean
) 