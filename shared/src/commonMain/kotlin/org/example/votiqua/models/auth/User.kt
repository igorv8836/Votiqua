package org.example.votiqua.models.auth

import kotlinx.serialization.Serializable

interface User {
    val id: Int
    val username: String
    val photoUrl: String?
    val isAdmin: Boolean
}

@Serializable
data class SimpleUser(
    val id: Int,
    val username: String,
    val photoUrl: String?
)