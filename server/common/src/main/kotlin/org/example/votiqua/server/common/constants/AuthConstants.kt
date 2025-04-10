package org.example.votiqua.server.common.constants

object AuthConstants {
    const val REALM = "Votiqua server"
    const val JWT_ISSUER = "ru.example.votiqua"
    const val JWT_SUBJECT = "Authentification"
    const val JWT_CLAIM = "email"

    const val BAN_TEXT = "Your account is banned. Please contact support."
}