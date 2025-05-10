package org.example.votiqua.server.feature.profile.data

class ProfilePhotoUrlConverterImpl(
    private val serverUrl: String
) : ProfilePhotoUrlConverter {
    override fun getUserPhotoUrl(imageName: String?, userId: Int): String {
        val uniquePostfix = imageName?.split(".")?.dropLast(1)?.lastOrNull()?.takeLast(3)
        return "$serverUrl/api/v1/profile/$userId/photo/${uniquePostfix ?: ""}"
    }
}