package org.example.votiqua.models.profile

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int,
    val username: String,
    val email: String,
    val photoUrl: String? = null,
    val description: String? = null,
    val pollsCreated: Int = 0,
    val pollsVoted: Int = 0
) 