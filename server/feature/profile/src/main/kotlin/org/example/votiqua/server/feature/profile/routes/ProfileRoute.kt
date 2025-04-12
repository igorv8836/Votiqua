package org.example.votiqua.server.feature.profile.routes

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.example.votiqua.server.common.utils.handleBadRequest
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.profile.data.ProfileRepository
import java.io.File
import java.util.UUID

@Serializable
data class ProfileUpdateRequest(
    val username: String? = null,
    val bio: String? = null
)

fun Route.profileRoute() {
    route("/profile") {
        // Получить профиль текущего пользователя
        get {
            val userId = call.requireAuthorization()
            val profile = ProfileRepository.getUserProfile(userId) ?: run {
                call.respond(HttpStatusCode.NotFound, "Profile not found")
                return@get
            }
            
            call.respond(HttpStatusCode.OK, profile)
        }
        
        // Получить профиль по ID
        get("/{userId}") {
            val targetUserId = call.parameters["userId"]?.toIntOrNull() ?: run {
                call.handleBadRequest("Invalid user ID")
                return@get
            }
            
            val profile = ProfileRepository.getUserProfile(targetUserId) ?: run {
                call.respond(HttpStatusCode.NotFound, "Profile not found")
                return@get
            }
            
            call.respond(HttpStatusCode.OK, profile)
        }
        
        // Обновить профиль
        patch {
            val userId = call.requireAuthorization()
            
            val updateRequest = try {
                call.receive<ProfileUpdateRequest>()
            } catch (e: Exception) {
                call.handleBadRequest("Invalid update data")
                return@patch
            }
            
            val updatedProfile = ProfileRepository.updateUserProfile(
                userId = userId,
                username = updateRequest.username,
                bio = updateRequest.bio
            ) ?: run {
                call.respond(HttpStatusCode.NotFound, "Profile not found")
                return@patch
            }
            
            call.respond(HttpStatusCode.OK, updatedProfile)
        }
        
        // Загрузить фото профиля
        post("/photo") {
            val userId = call.requireAuthorization()
            
            val multipart = call.receiveMultipart()
            var fileName: String? = null
            
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val originalFileName = part.originalFileName ?: "photo.jpg"
                        val fileExtension = originalFileName.substringAfterLast('.', "jpg")
                        
                        // Создаем уникальное имя файла
                        fileName = "${UUID.randomUUID()}-$userId.$fileExtension"
                        
                        // Путь к директории для хранения фотографий профилей
                        val uploadDir = File("uploads/profile_photos")
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs()
                        }
                        
                        // Сохраняем файл
                        val file = File(uploadDir, fileName!!)
                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }
            
            if (fileName == null) {
                call.handleBadRequest("No file uploaded")
                return@post
            }
            
            // URL для доступа к фотографии
            val photoUrl = "/uploads/profile_photos/$fileName"
            
            // Обновляем профиль пользователя с новым URL фотографии
            val updatedProfile = ProfileRepository.updateUserProfile(
                userId = userId,
                photoUrl = photoUrl
            ) ?: run {
                call.respond(HttpStatusCode.NotFound, "Profile not found")
                return@post
            }
            
            call.respond(HttpStatusCode.OK, updatedProfile)
        }
    }
}