package org.example.votiqua.server.feature.profile.routes

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.request.contentType
import io.ktor.server.request.receiveChannel
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.models.common.ErrorType
import org.example.votiqua.models.profile.ProfileUpdateRequest
import org.example.votiqua.server.common.utils.handleBadRequest
import org.example.votiqua.server.common.utils.receiveOrException
import org.example.votiqua.server.common.utils.requireAuthorization
import org.example.votiqua.server.feature.profile.data.ProfileRepository
import org.example.votiqua.server.feature.profile.domain.ProfilePhotoUseCase
import org.koin.ktor.ext.inject

fun Route.profileRoute() {
    val profilePhotoUseCase: ProfilePhotoUseCase by application.inject()
    val profileRepository: ProfileRepository by application.inject()

    route("/profile") {
        authenticate("jwt") {
            get {
                val userId = call.requireAuthorization()
                val profile = profileRepository.getUserProfile(userId) ?: run {
                    call.respondNotFoundUser()
                    return@get
                }

                call.respond(HttpStatusCode.OK, profile)
            }

            patch {
                val userId = call.requireAuthorization()
                val updateRequest = call.receiveOrException<ProfileUpdateRequest>()

                val updatedProfile = profileRepository.updateUserProfile(
                    userId = userId,
                    username = updateRequest.username,
                    description = updateRequest.description
                ) ?: run {
                    call.respondNotFoundUser()
                    return@patch
                }

                call.respond(HttpStatusCode.OK, updatedProfile)
            }

            post("/photo") {
                val userId = call.requireAuthorization()

                val channel: ByteReadChannel = call.receiveChannel()
                val bytes: ByteArray = channel.readRemaining().readByteArray()

                val ct: ContentType = call.request.contentType()
                if (!(ct == ContentType.Image.PNG || ct == ContentType.Image.JPEG) || ct.contentSubtype.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse("No valid update data provided"))
                    return@post
                }
                val fileExtension: String = ct.contentSubtype

                val updatedProfile = profilePhotoUseCase.updateUserPhoto(userId, bytes, fileExtension)

                call.respond(HttpStatusCode.OK, updatedProfile)
            }
        }
        
        get("/{userId}") {
            val targetUserId = call.parameters["userId"]?.toIntOrNull() ?: run {
                call.handleBadRequest("Invalid user ID")
                return@get
            }
            
            val profile = profileRepository.getUserProfile(targetUserId) ?: run {
                call.respondNotFoundUser()
                return@get
            }
            
            call.respond(HttpStatusCode.OK, profile)
        }

        get("/{userId}/photo/{id}") {
            val targetUserId = call.parameters["userId"]?.toIntOrNull() ?: run {
                call.handleBadRequest("Invalid user ID")
                return@get
            }

            try {
                val photoUrl = profilePhotoUseCase.getUserPhotoUrl(targetUserId)
                val photoFile = profilePhotoUseCase.getPhotoFile(photoUrl)

                call.respondFile(photoFile)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound, BaseResponse("Photo not found"))
            }
        }
    }
}

private suspend fun ApplicationCall.respondNotFoundUser() {
    respond(HttpStatusCode.NotFound, BaseResponse(ErrorType.PROFILE_NOT_FOUND.message))
}