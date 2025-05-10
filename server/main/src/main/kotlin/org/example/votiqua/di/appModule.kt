package org.example.votiqua.di

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig
import org.koin.dsl.module
import java.io.File

fun appModule() = module {
    single<HoconApplicationConfig> {
        val configFile = File("application.conf")
        val hoconConfig = ConfigFactory.parseFile(configFile)
        HoconApplicationConfig(hoconConfig)
    }
}
