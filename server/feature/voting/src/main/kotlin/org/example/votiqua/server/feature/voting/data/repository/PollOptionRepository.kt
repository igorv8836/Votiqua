package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.poll.PollOption
import org.example.votiqua.server.feature.voting.database.PollOptionTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PollOptionRepository {
    fun getOptions(pollId: Int): List<PollOption> {
        return PollOptionTable
            .select { PollOptionTable.pollId eq pollId }
            .orderBy(PollOptionTable.orderIndex)
            .map { mapRowToPollOption(it) }
    }

    fun insertOptions(
        pollId: Int,
        options: List<PollOption>,
    ) {
        options.forEach { option ->
            PollOptionTable.insert {
                it[PollOptionTable.pollId] = pollId
                it[optionText] = option.optionText
                it[orderIndex] = option.orderIndex
            }
        }
    }

    fun updateOptions(
        pollId: Int,
        options: List<PollOption>,
    ) {
        PollOptionTable.deleteWhere { PollOptionTable.pollId eq pollId }
        options.forEach { option ->
            PollOptionTable.insert {
                it[PollOptionTable.pollId] = pollId
                it[PollOptionTable.optionText] = option.optionText
                it[PollOptionTable.orderIndex] = option.orderIndex
            }
        }
    }

    private fun mapRowToPollOption(row: ResultRow): PollOption {
        return PollOption(
            id = row[PollOptionTable.id],
            pollId = row[PollOptionTable.pollId],
            optionText = row[PollOptionTable.optionText],
            orderIndex = row[PollOptionTable.orderIndex],
        )
    }
}