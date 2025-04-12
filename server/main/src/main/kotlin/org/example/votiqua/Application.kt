package org.example.votiqua

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import kotlinx.coroutines.runBlocking
import org.example.votiqua.plugins.DatabaseFactory.initializationDatabase
import org.example.votiqua.plugins.configureCors
import org.example.votiqua.plugins.configureFileUpload
import org.example.votiqua.plugins.configureKoin
import org.example.votiqua.plugins.configureRouting
import org.example.votiqua.plugins.configureSecurity
import org.example.votiqua.plugins.configureSerialization
import org.example.votiqua.plugins.configureStatusPages
import org.example.votiqua.utils.PollSeeder
import org.koin.ktor.ext.get
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
    configureKoin()

    install(ForwardedHeaders)
    install(XForwardedHeaders) { useFirstProxy() }
    install(CallLogging) { level = Level.TRACE }

    val config = get<HoconApplicationConfig>()

    configureStatusPages()
    configureCors()
    configureFileUpload()
    configureSecurity()
    configureSerialization()
    initializationDatabase(config)
    configureRouting(config)

    runBlocking {
        PollSeeder.seedPolls(1000)
    }
}