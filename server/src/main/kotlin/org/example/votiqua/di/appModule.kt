package org.example.votiqua.di

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig
import org.example.votiqua.data.auth.JwtService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

fun appModule() = module {
    single(named("")) { "" }
    single { JwtService(get()) }

    single<HoconApplicationConfig> {
        val configFile = File("application.conf")
        val hoconConfig = ConfigFactory.parseFile(configFile)
        HoconApplicationConfig(hoconConfig)
    }
}