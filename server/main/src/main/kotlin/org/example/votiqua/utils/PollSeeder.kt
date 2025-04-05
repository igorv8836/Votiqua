package org.example.votiqua.utils

import io.github.serpro69.kfaker.Faker
import org.example.votiqua.database.tables.PollTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime


object PollSeeder {
    private val faker = Faker()

    fun seedPolls(count: Int = 10) {
        transaction {
            val pollCount = PollTable.selectAll().count()
            if (pollCount > 0) {
                return@transaction
            }

            repeat(count) {
                PollTable.insert {
                    it[question] = faker.quote.famousLastWords()
                    it[description] = faker.quote.famousLastWords()
                    it[isMultiple] = faker.random.nextBoolean()
                    it[isAnonymous] = faker.random.nextBoolean()
                    it[isOpen] = faker.random.nextBoolean()
                    it[createdAt] = LocalDateTime.now()
                    it[startDate] = LocalDateTime.now().plusDays(faker.random.nextLong(100))
                    it[endDate] = LocalDateTime.now().plusDays(faker.random.nextLong(100))
                }
            }
        }
    }
}