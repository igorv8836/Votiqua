package org.example.votiqua.server.feature.profile.routes

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.utils.io.toByteArray
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

                val multipart = call.receiveMultipart()
                var photo: ByteArray? = null
                var fileExtension: String? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            if (part.name == "photo") {
                                photo = part.provider().toByteArray()
                                fileExtension = part.originalFileName?.substringAfterLast('.')
                            }
                            part.dispose()
                        }
                        else -> part.dispose()
                    }
                }

                val updatedProfile = if (photo != null && fileExtension != null) {
                    profilePhotoUseCase.updateUserPhoto(userId, photo!!, fileExtension!!)
                } else {
                    return@post call.respond(HttpStatusCode.BadRequest, BaseResponse("No valid update data provided"))
                }

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

        get("/{userId}/photo") {
            val targetUserId = call.parameters["userId"]?.toIntOrNull() ?: run {
                call.handleBadRequest("Invalid user ID")
                return@get
            }

            try {
                val photoUrl = profilePhotoUseCase.getOtherUserPhotoUrl(targetUserId)
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