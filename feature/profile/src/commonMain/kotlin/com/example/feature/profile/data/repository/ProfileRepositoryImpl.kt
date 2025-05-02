package com.example.feature.profile.data.repository

import com.example.common.ThemeMode
import com.example.feature.profile.api.data.repository.ProfileRepository
import com.example.feature.profile.api.domain.User
import com.example.votiqua.datastore.settings.SettingsDataStore
import io.ktor.utils.io.core.Input
import kotlinx.coroutines.flow.Flow
import org.example.votiqua.models.profile.UserProfile

internal class ProfileRepositoryImpl(
    private val settingsDataStore: SettingsDataStore,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {
    override fun getThemeMode(): Flow<ThemeMode> {
        return settingsDataStore.appTheme
    }

    override suspend fun setThemeMode(mode: ThemeMode): Result<Unit> {
        return runCatching {
            settingsDataStore.setAppTheme(mode)
        }
    }

    override suspend fun getUserProfile(): Result<User> {
        return profileRemoteDataSource.getUserProfile().toUser()
    }

    override suspend fun updateUserProfile(username: String?, description: String?): Result<User> {
        return profileRemoteDataSource.updateUserProfile(username, description).toUser()
    }

    override suspend fun updateUserPhoto(photo: Input, fileExtension: String): Result<User> {
        return profileRemoteDataSource.updateUserPhoto(photo, fileExtension).toUser()
    }

    override suspend fun getOtherUserProfile(userId: Int): Result<User> {
        return profileRemoteDataSource.getOtherUserProfile(userId).toUser()
    }

    override suspend fun getOtherUserPhoto(userId: Int): Result<ByteArray> {
        return profileRemoteDataSource.getOtherUserPhoto(userId)
    }

    private fun UserProfile.toUser(): User {
        return User(
            email = this.email,
            nickname = this.username,
            photoUrl = this.photoUrl
        )
    }

    private fun Result<UserProfile>.toUser(): Result<User> {
        return this.map { it.toUser() }
    }
}