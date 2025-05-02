package com.example.feature.profile.data.repository

import com.example.votiqua.network.util.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.readBytes
import org.example.votiqua.models.profile.ProfileUpdateRequest
import org.example.votiqua.models.profile.UserProfile

internal class ProfileRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getUserProfile(): Result<UserProfile> = safeApiCall {
        httpClient.get("profile")
    }

    suspend fun updateUserProfile(
        username: String? = null,
        description: String? = null
    ): Result<UserProfile> = safeApiCall {
        httpClient.patch("profile") {
            setBody(ProfileUpdateRequest(username, description))
        }
    }

    suspend fun updateUserPhoto(photo: Input, fileExtension: String): Result<UserProfile> = safeApiCall {
        httpClient.post("profile/photo") {
            setBody(photo.readBytes())
        }
    }

    suspend fun getOtherUserProfile(userId: Int): Result<UserProfile> = safeApiCall {
        httpClient.get("profile/$userId")
    }

    suspend fun getOtherUserPhoto(userId: Int): Result<ByteArray> = safeApiCall {
        httpClient.get("profile/$userId/photo")
    }
}