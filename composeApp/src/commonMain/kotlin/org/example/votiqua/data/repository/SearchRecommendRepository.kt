package org.example.votiqua.data.repository

import org.example.votiqua.data.network.SearchRecommendRemoteDataSource
import org.example.votiqua.network.search.PollSearchResponse

class SearchRecommendRepository(
    private val remoteDataSource: SearchRecommendRemoteDataSource,
) {
    suspend fun searchPolls(query: String): PollSearchResponse {
        return remoteDataSource.searchPolls(query)
    }
}