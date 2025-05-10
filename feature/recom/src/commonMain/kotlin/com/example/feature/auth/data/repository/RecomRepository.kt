package com.example.feature.auth.data.repository

import com.example.feature.auth.data.network.RecomRemoteDataSource
import com.example.feature.auth.domain.QueryRecommendModel
import com.example.feature.auth.domain.QueryType
import com.example.votiqua.datastore.search.SearchDataStore
import kotlinx.coroutines.flow.first
import org.example.votiqua.models.recom.MainScreenResponse
import org.example.votiqua.models.search.PollSearchResponse

class RecomRepository(
    private val remoteDataSource: RecomRemoteDataSource,
    private val localDataSource: SearchDataStore,
) {
    suspend fun getHistoryQueries(): List<QueryRecommendModel> {
        return localDataSource.searchQueries.first().map { QueryRecommendModel(it, QueryType.HISTORY) }
    }

    suspend fun deleteQueryFromHistory(query: String) {
        return localDataSource.clearQuery(query)
    }

    suspend fun getQueryRecommends(
        query: String,
        useLastResponse: Boolean = false,
    ): List<QueryRecommendModel> {
        val localQueries = localDataSource.searchQueries.first().toList()
            .map { QueryRecommendModel(it, QueryType.HISTORY) }
        val remoteQueries = if (!useLastResponse) {
            remoteDataSource.searchPolls(query).results
                .map { QueryRecommendModel(it, QueryType.RECOMMEND) }
        } else {
            remoteDataSource.lastResponse?.results
                ?.map { QueryRecommendModel(it, QueryType.RECOMMEND) } ?: emptyList()
        }

        return localQueries + remoteQueries
    }

    suspend fun getPolls(query: String): List<PollSearchResponse> {
        localDataSource.addQuery(query)

        return emptyList()
    }

    suspend fun getMainScreenResponse(): Result<MainScreenResponse> {
        return remoteDataSource.getMainScreenPolls()
    }
}