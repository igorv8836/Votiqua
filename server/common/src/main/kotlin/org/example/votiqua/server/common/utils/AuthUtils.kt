package org.example.votiqua.server.common.utils

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.principal
import org.example.votiqua.server.common.models.HTTPUnauthorizedException
import org.example.votiqua.server.common.models.auth.UserModel

suspend fun ApplicationCall.requireAuthorization(): Int {
    val user = principal<UserModel>() ?: run {
        throw HTTPUnauthorizedException()
    }
    return user.id
}

fun ApplicationCall.getUserId(): Int? {
    return principal<UserModel>()?.id
} 