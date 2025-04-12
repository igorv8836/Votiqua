package org.example.votiqua.server.feature.profile.domain

object ProfilePhotoConfig {
    const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024 // 5MB
    val ALLOWED_EXTENSIONS = listOf("jpg", "jpeg", "png")
    const val UPLOAD_DIR = "uploads/profile_photos"
    const val PHOTO_URL_PREFIX = "/uploads/profile_photos"
} 