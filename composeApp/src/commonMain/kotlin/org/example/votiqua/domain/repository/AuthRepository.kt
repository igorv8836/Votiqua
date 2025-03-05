package org.example.votiqua.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.votiqua.domain.model.UserModel

interface AuthRepository {
    suspend fun signUp(email: String, password: String, username: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): Result<String>

    suspend fun resetPassword(email: String, resetCode: Int, newPassword: String): Result<String>
    suspend fun sendResetCode(email: String): Result<String>
    suspend fun getUser(): Result<UserModel>
    fun getToken(): Flow<String?>
    suspend fun logout(): Result<Unit>
}