package com.example.feature.auth.data

import com.example.votiqua.network.util.addAuthToken
import com.example.votiqua.network.util.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.votiqua.models.auth.LoginRequest
import org.example.votiqua.models.auth.PasswordRecoveryRequest
import org.example.votiqua.models.auth.PasswordResetRequest
import org.example.votiqua.models.auth.RegisterRequest
import org.example.votiqua.models.auth.TokenResponse
import org.example.votiqua.models.auth.UsernameCheckResponse
import org.example.votiqua.models.common.BaseResponse

internal class AuthRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun login(email: String, password: String): Result<TokenResponse> {
        return safeApiCall {
            httpClient.post("auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
        }
    }

    suspend fun register(email: String, password: String, username: String): Result<TokenResponse> {
        return safeApiCall {
            httpClient.post("auth/register") {
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterRequest(
                        email = email,
                        password = password,
                        username = username,
                    )
                )
            }
        }
    }

    suspend fun sendResetCode(email: String): Result<String> {
        return safeApiCall {
            httpClient.post("auth/send_reset_code") {
                contentType(ContentType.Application.Json)
                setBody(PasswordRecoveryRequest(email))
            }
        }
    }

    suspend fun resetPassword(email: String, code: Int, newPassword: String): Result<String> {
        return safeApiCall {
            httpClient.post("auth/password_reset") {
                contentType(ContentType.Application.Json)
                setBody(PasswordResetRequest(email, code, newPassword))
            }
        }
    }

    suspend fun isUsernameTaken(username: String): Result<UsernameCheckResponse> {
        return safeApiCall {
            httpClient.get("auth/is-username-taken/$username")
        }
    }

    suspend fun checkToken(token: String): Result<BaseResponse<Boolean>> {
        return safeApiCall {
            httpClient.addAuthToken(token)
            httpClient.get("auth/check-token")
        }
    }
}