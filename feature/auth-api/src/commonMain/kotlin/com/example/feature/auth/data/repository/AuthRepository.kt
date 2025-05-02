package com.example.feature.auth.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(email: String, password: String, username: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Result<String>

    suspend fun resetPassword(email: String, resetCode: Int, newPassword: String): Result<String>
    suspend fun sendResetCode(email: String): Result<String>
    suspend fun checkToken(): Result<Boolean>
    fun getToken(): Flow<String?>
    suspend fun logout(): Result<Unit>
    suspend fun isUsernameTaken(username: String): Result<Boolean>
}