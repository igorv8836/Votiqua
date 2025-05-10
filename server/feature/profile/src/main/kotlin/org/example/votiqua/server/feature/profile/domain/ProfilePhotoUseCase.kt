package org.example.votiqua.server.feature.profile.domain

import org.example.votiqua.models.common.ErrorType
import org.example.votiqua.models.profile.UserProfile
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.feature.profile.data.ProfileRepository
import java.io.File
import java.util.UUID

class ProfilePhotoUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend fun updateUserPhoto(
        userId: Int,
        photo: ByteArray,
        fileExtension: String
    ): UserProfile {
        if (photo.size > ProfilePhotoConfig.MAX_FILE_SIZE_BYTES) {
            throw HTTPConflictException(ErrorType.FILE_TOO_LARGE_5MB.message)
        }

        if (fileExtension.lowercase() !in ProfilePhotoConfig.ALLOWED_EXTENSIONS) {
            throw HTTPConflictException(ErrorType.INVALID_FILE_TYPE.message)
        }

        val randomString = UUID.randomUUID().toString().take(8)

        val fileName = "$userId-$randomString.$fileExtension"
        val uploadDir = File(ProfilePhotoConfig.UPLOAD_DIR)
        if (!uploadDir.exists()) {
            uploadDir.mkdirs()
        }

        val file = File(uploadDir, fileName)
        file.writeBytes(photo)

        val photoUrl = "${ProfilePhotoConfig.PHOTO_URL_PREFIX}/$fileName"
        
        return profileRepository.updateUserProfile(
            userId = userId,
            photoUrl = photoUrl
        ) ?: throw HTTPConflictException(ErrorType.PROFILE_NOT_FOUND.message)
    }

    suspend fun getUserPhotoUrl(userId: Int): String {
        val profile = profileRepository.getUserProfile(userId)
            ?: throw HTTPConflictException(ErrorType.PROFILE_NOT_FOUND.message)

        return profile.originalPhotoUrl ?: throw HTTPConflictException(ErrorType.PHOTO_NOT_FOUND.message)
    }

    suspend fun getPhotoFile(photoUrl: String): File {
        val fileName = photoUrl.substringAfterLast('/')
        val file = File(ProfilePhotoConfig.UPLOAD_DIR, fileName)

        if (!file.exists()) {
            throw HTTPConflictException(ErrorType.PHOTO_NOT_FOUND.message)
        }

        return file
    }
}