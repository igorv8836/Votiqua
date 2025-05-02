package com.example.feature.auth.data

import com.example.feature.auth.data.repository.AuthRepository
import com.example.votiqua.datastore.auth.TokenDataStore
import com.example.votiqua.network.model.NetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

internal class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Result<Unit> {
        return remoteDataSource.register(email, password, username)
            .map { tokenResponse ->
                tokenDataStore.saveToken(tokenResponse.token)
            }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return remoteDataSource.login(email, password)
            .map { tokenResponse ->
                tokenDataStore.saveToken(tokenResponse.token)
            }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Result<String> {
        return remoteDataSource.changePassword(
            lastPassword = oldPassword,
            newPassword = newPassword,
        ).map { it.message }
    }

    override suspend fun resetPassword(
        email: String,
        resetCode: Int,
        newPassword: String
    ): Result<String> {
        return remoteDataSource.resetPassword(email, resetCode, newPassword)
    }

    override suspend fun sendResetCode(email: String): Result<String> {
        return remoteDataSource.sendResetCode(email)
    }

    override suspend fun checkToken(): Result<Boolean> {
        val token = tokenDataStore.tokenFlow.firstOrNull()
            ?: return Result.success(false)

        return try {
            val response = remoteDataSource.checkToken(token).getOrThrow()
            Result.success(response.message)
        } catch (_: NetworkException.Unauthorized) {
            Result.success(false)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getToken(): Flow<String?> {
        return tokenDataStore.tokenFlow
    }

    override suspend fun logout(): Result<Unit> {
        tokenDataStore.clearToken()
        return Result.success(Unit)
    }

    override suspend fun isUsernameTaken(username: String): Result<Boolean> {
        return remoteDataSource.isUsernameTaken(username)
            .map { it.isUsed }
    }
}