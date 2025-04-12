package org.example.votiqua.utils

import io.github.serpro69.kfaker.Faker
import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.feature.voting.database.PollTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


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
                    it[createdAt] = currentDateTime()
                    it[startDate] = currentDateTime().plusDays(faker.random.nextLong(100))
                    it[endDate] = currentDateTime().plusDays(faker.random.nextLong(100))
                }
            }
        }
    }
}