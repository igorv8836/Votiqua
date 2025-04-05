package org.example.votiqua.server.feature.auth.data

import io.ktor.server.config.*
import org.apache.commons.mail.Email
import org.apache.commons.mail.HtmlEmail

class EmailService(
    private val config: HoconApplicationConfig,
) {

    fun sendMessage(
        targetEmail: String,
        message: String,
        subject: String,
    ): Result<Unit> = runCatching {
        val email: Email = HtmlEmail()

        val username = config.property("ktor.email.username").getString()
        val password = config.property("ktor.email.password").getString()

        email.hostName = config.property("ktor.email.hostname").getString()
        email.setSmtpPort(config.property("ktor.email.smtpPort").getString().toInt())
        email.setAuthentication(username, password)
        email.isSSLOnConnect = true
        email.setFrom(username)
        email.addTo(targetEmail)
        email.subject = subject
        email.setMsg(message)

        email.send()
    }
}