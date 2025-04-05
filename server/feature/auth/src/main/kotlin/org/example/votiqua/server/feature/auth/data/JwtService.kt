package org.example.votiqua.server.feature.auth.data

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.example.votiqua.server.common.models.auth.UserModel
import java.time.LocalDateTime
import java.time.ZoneOffset

class JwtService(
    jwtSecret: String,
) {

    private val issuer = "ru.example.votiqua"
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    private val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: UserModel): String {
        return JWT.create()
            .withSubject("Authentification")
            .withIssuer(issuer)
            .withClaim("email", user.email)
            .withExpiresAt(LocalDateTime.now().plusDays(90).toInstant(ZoneOffset.UTC))
            .sign(algorithm)
    }

    fun getVerifier(): JWTVerifier = verifier
}