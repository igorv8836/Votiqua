package org.example.votiqua.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.example.votiqua.models.common.BaseResponse
import org.example.votiqua.server.common.constants.AuthConstants
import org.example.votiqua.server.feature.auth.domain.usecases.JwtUseCase
import org.example.votiqua.server.feature.auth.domain.usecases.UserUseCase
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val jwtUseCase by inject<JwtUseCase>()
    val userUseCase by inject<UserUseCase>()
    authentication {
        jwt("jwt") {
            verifier(jwtUseCase.getJwtVerifier())
            realm = AuthConstants.REALM
            validate {
                val payload = it.payload
                val email = payload.getClaim(AuthConstants.JWT_CLAIM).asString()
                val user = userUseCase.findUserByEmail(email = email)
                if (user?.isActive == false){
                    this.respond(
                        status = HttpStatusCode.Forbidden,
                        message = BaseResponse(user.banReason ?: AuthConstants.BAN_TEXT),
                    )
                    return@validate null
                }
                user
            }
        }
    }
}