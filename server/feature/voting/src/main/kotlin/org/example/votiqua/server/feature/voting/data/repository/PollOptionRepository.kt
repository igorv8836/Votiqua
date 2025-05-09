package org.example.votiqua.server.feature.voting.data.repository

import org.example.votiqua.models.poll.PollOption
import org.example.votiqua.server.feature.voting.database.PollOptionTable
import org.example.votiqua.server.feature.voting.database.VoteTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PollOptionRepository {
    fun getOptions(pollId: Int): List<PollOption> {
        val options = PollOptionTable
            .select { PollOptionTable.pollId eq pollId }
            .orderBy(PollOptionTable.orderIndex)
            .map { mapRowToPollOption(it) }
            
        val totalVotes = countTotalVotes(pollId)
        
        return options.map { option ->
            val voteCount = countVotesForOption(option.id)
            val percentage = if (totalVotes > 0) (voteCount.toDouble() / totalVotes) * 100 else 0.0
            
            option.copy(
                voteCount = voteCount,
                percentage = percentage
            )
        }
    }

    private fun countVotesForOption(optionId: Int): Int {
        return VoteTable
            .select { VoteTable.optionId eq optionId }
            .count()
            .toInt()
    }
    
    private fun countTotalVotes(pollId: Int): Int {
        return VoteTable
            .select { VoteTable.pollId eq pollId }
            .count()
            .toInt()
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
            voteCount = 0,
            percentage = 0.0,
        )
    }
}