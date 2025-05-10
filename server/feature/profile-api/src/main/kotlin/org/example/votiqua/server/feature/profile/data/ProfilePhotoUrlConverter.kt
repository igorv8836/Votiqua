package org.example.votiqua.server.feature.profile.data

interface ProfilePhotoUrlConverter {
    fun getUserPhotoUrl(imageName: String?, userId: Int): String
}