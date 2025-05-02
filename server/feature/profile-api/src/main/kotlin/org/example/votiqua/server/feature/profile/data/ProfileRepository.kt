package org.example.votiqua.server.feature.profile.data

import org.example.votiqua.models.profile.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(userId: Int): UserProfile?
    suspend fun updateUserProfile(
        userId: Int,
        username: String? = null,
        description: String? = null,
        photoUrl: String? = null
    ): UserProfile?
    suspend fun searchUsers(query: String, limit: Int = 10): List<UserProfile>
}