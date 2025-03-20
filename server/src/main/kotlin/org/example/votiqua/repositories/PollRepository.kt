package org.example.votiqua.repositories

import org.example.votiqua.database.tables.PollTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object PollRepository {
    fun searchPollTitles(query: String, limit: Int = 10): List<String> {
        return transaction {
            PollTable.select { PollTable.question like "%$query%" }
                .limit(limit)
                .map { it[PollTable.question] }
        }
    }
}