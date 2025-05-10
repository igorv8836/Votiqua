package org.example.votiqua.server.feature.recom.data.repository

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.models.recom.MainScreenResponse
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.feature.recom.database.AdminMessageTable
import org.example.votiqua.server.feature.voting.data.repository.PollRepository
import org.example.votiqua.server.feature.voting.data.repository.SortingType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select

class RecomRepository(private val pollRepository: PollRepository) {
    
    suspend fun getNewPolls(limit: Int): List<Poll> {
        return pollRepository
            .getPolls(limit, sortField = SortingType.CreateDate, sortOrder = SortOrder.DESC)
            .filter { it.isOpen }
    }
    
    suspend fun getPopularPolls(limit: Int): List<Poll> {
        return pollRepository.getPopularPolls(limit)
            .filter { it.isOpen }
    }
    
    suspend fun getAdminMessages(): List<String> {
        return dbQuery {
            AdminMessageTable
                .select { AdminMessageTable.isActive eq true }
                .orderBy(AdminMessageTable.createdAt, SortOrder.DESC)
                .map { it[AdminMessageTable.message] }
        }
    }
    
    suspend fun getMainScreenData(limit: Int = 10): MainScreenResponse {
        val newPolls = getNewPolls(limit)
        val popularPolls = getPopularPolls(limit)
        val adminMessages = getAdminMessages()
        
        return MainScreenResponse(
            newPolls = newPolls,
            popularPolls = popularPolls,
            adminMessages = adminMessages
        )
    }
}