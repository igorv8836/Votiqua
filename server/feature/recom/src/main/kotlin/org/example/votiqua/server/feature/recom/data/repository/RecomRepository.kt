package org.example.votiqua.server.feature.recom.data.repository

import org.example.votiqua.models.poll.Poll
import org.example.votiqua.models.recom.MainScreenResponse
import org.example.votiqua.server.common.utils.dbQuery
import org.example.votiqua.server.feature.recom.database.AdminMessageTable
import org.example.votiqua.server.feature.voting.data.repository.SortingType
import org.example.votiqua.server.feature.voting.domain.usecase.GetPollUseCase
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select

class RecomRepository(
    private val getPollUseCase: GetPollUseCase
) {
    
    suspend fun getNewPolls(limit: Int, userId: Int): List<Poll> {
        return getPollUseCase
            .getPolls(limit, sortField = SortingType.CreateDate, sortOrder = SortOrder.DESC, userId = userId)
    }
    
    suspend fun getPopularPolls(limit: Int, userId: Int): List<Poll> {
        return getPollUseCase.getPopularPolls(limit, userId = userId)
    }
    
    suspend fun getAdminMessages(): List<String> {
        return dbQuery {
            AdminMessageTable
                .select { AdminMessageTable.isActive eq true }
                .orderBy(AdminMessageTable.createdAt, SortOrder.DESC)
                .map { it[AdminMessageTable.message] }
        }
    }
    
    suspend fun getMainScreenData(limit: Int = 10, userId: Int): MainScreenResponse {
        val newPolls = getNewPolls(limit, userId)
        val popularPolls = getPopularPolls(limit, userId)
        val adminMessages = getAdminMessages()
        
        return MainScreenResponse(
            newPolls = newPolls,
            popularPolls = popularPolls,
            adminMessages = adminMessages
        )
    }
}