package org.example.votiqua.server.feature.profile.data

class ProfilePhotoUrlConverterImpl(
    private val serverUrl: String
) : ProfilePhotoUrlConverter {
    override fun getUserPhotoUrl(userId: Int): String {
        return "$serverUrl/api/v1/profile/$userId/photo"
    }
}