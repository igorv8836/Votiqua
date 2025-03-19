package org.example.votiqua

import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import org.example.votiqua.di.appModule
import org.example.votiqua.plugins.DatabaseFactory.initializationDatabase
import org.example.votiqua.plugins.configureRouting
import org.example.votiqua.plugins.configureSecurity
import org.example.votiqua.plugins.configureSerialization
import org.example.votiqua.resources.ErrorType
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level
import java.io.File

fun main() {
    val configFile = File("application.conf")
    val hoconConfig = ConfigFactory.parseFile(configFile)
    val config = HoconApplicationConfig(hoconConfig)

    val host = config.property("ktor.deployment.host").getString()
    val port = config.property("ktor.deployment.port").getString().toInt()
    embeddedServer(Netty, port = port, host = host, module = Application::module).start(wait = true)
}

fun Application.module() {
    val configFile = File("application.conf")
    val hoconConfig = ConfigFactory.parseFile(configFile)
    val config = HoconApplicationConfig(hoconConfig)

    install(Koin) {
        modules(
            appModule,
        )
    }
    install(ForwardedHeaders)
    install(XForwardedHeaders) {
        useFirstProxy()
    }

    install(CallLogging){
        level = Level.TRACE
    }

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respondRedirect("")
        }
        exception<ContentTransformationException> { call, exception ->
            call.respond(
                HttpStatusCode.BadRequest,
                exception.message ?: ErrorType.GENERAL.message
            )
        }

        exception<Exception> { call, exception ->
            call.respond(HttpStatusCode.BadRequest, exception.message ?: ErrorType.GENERAL.message)
        }
    }

    configureSecurity()
    configureSerialization()
    initializationDatabase(config)
    configureRouting(config)
}