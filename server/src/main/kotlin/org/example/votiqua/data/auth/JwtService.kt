package org.example.votiqua.data.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

class JwtService(
    jwtSecret: String,
) {

    private val issuer = "ru.example.votiqua"
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    private val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

//    fun generateToken(user: UserModel): String {
//        return JWT.create()
//            .withSubject("Authentification")
//            .withIssuer(issuer)
//            .withClaim("email", user.email)
//            .withExpiresAt(LocalDateTime.now().plusDays(90).toInstant(ZoneOffset.UTC))
//            .sign(algorithm)
//    }

    fun getVerifier(): JWTVerifier = verifier
}