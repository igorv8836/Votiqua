package org.example.votiqua.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import org.example.votiqua.database.utils.createDbTables
import org.example.votiqua.database.utils.createMissingColumns
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun Application.initializationDatabase(myConfig: ApplicationConfig) {
        Database.connect(getHikariDatasource(myConfig))

        transaction {
            createDbTables()
            createMissingColumns()
        }
    }

    private fun getHikariDatasource(myConfig: ApplicationConfig): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = myConfig.property("storage.driverClassName").getString()
        config.jdbcUrl = myConfig.property("storage.jdbcURL").getString()
        config.username = myConfig.property("storage.user").getString()
        config.password = myConfig.property("storage.password").getString()
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }
}