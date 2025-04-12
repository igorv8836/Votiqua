package org.example.votiqua.plugins

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import java.io.File

fun Application.configureFileUpload() {
    val uploadDir = File("uploads")
    if (!uploadDir.exists()) {
        uploadDir.mkdirs()
    }
    
    val profilePhotosDir = File("uploads/profile_photos")
    if (!profilePhotosDir.exists()) {
        profilePhotosDir.mkdirs()
    }
}

fun Application.configureCors() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)

        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)

        anyHost()
    }
} 