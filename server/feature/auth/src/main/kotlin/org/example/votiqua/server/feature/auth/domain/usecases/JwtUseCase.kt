package org.example.votiqua.server.feature.auth.domain.usecases

import com.auth0.jwt.JWTVerifier
import org.example.votiqua.server.common.models.auth.UserModel
import org.example.votiqua.server.feature.auth.data.JwtService

class JwtUseCase(private val jwtService: JwtService) {

    fun getJwtVerifier(): JWTVerifier = jwtService.getVerifier()
    fun generateToken(userModel: UserModel): String {
        if (!userModel.isActive) return ""
        return jwtService.generateToken(userModel)
    }
}