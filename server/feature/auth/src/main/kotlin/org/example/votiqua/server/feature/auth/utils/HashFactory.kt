package org.example.votiqua.server.feature.auth.utils

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val hashKey = "+1wQMzx9gc7/JuYc2+CUMgbgPIUbYjjc78/E2vLyfpfzGFm6CJSwyjwNKM808R1p U6BIYhPYDIloECko+GBwkQ==".toByteArray()

class HashFactory(
    hashKey: ByteArray,
) {
    private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

    fun hash(password: String): String {
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(hmacKey)

        return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
    }
}