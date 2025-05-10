package org.example.votiqua.server.feature.profile.data

interface ProfilePhotoUrlConverter {
    fun getUserPhotoUrl(userId: Int): String
}