package org.example.votiqua.data.repository

import kotlinx.coroutines.flow.flow
import org.example.votiqua.domain.model.UserModel
import org.example.votiqua.domain.repository.AuthRepository


internal class AuthRepositoryImpl() : AuthRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): Result<String> {
        return Result.success("Успешно")
    }

    override suspend fun resetPassword(
        email: String,
        resetCode: Int,
        newPassword: String
    ): Result<String> {
        return Result.success("Успешно")
    }

    override suspend fun sendResetCode(email: String): Result<String> {
        return Result.success("Успешно")
    }

    override suspend fun getUser(): Result<UserModel> {
        return Result.success(
            UserModel(
                email = "email",
                username = "username",
                photoUrl = null,
                notificationEnabled = true,
                isActive = true,
                banReason = null,
            )
        )
    }

    override fun getToken() = flow {
        emit("token")
    }

    override suspend fun logout(): Result<Unit> {
        return Result.success(Unit)
    }
}