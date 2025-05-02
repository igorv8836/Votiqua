package org.example.votiqua.models.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateRequest(
    val username: String? = null,
    val description: String? = null
)