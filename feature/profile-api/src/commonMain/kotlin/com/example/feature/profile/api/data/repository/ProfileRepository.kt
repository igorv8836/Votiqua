package com.example.feature.profile.api.data.repository

import com.example.common.ThemeMode
import com.example.feature.profile.api.domain.User
import io.ktor.utils.io.core.Input
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode): Result<Unit>
    suspend fun getUserProfile(): Result<User>
    suspend fun updateUserProfile(username: String?, description: String?): Result<User>
    suspend fun updateUserPhoto(photo: Input, fileExtension: String): Result<User>
    suspend fun getOtherUserProfile(userId: Int): Result<User>
    suspend fun getOtherUserPhoto(userId: Int): Result<ByteArray>
}