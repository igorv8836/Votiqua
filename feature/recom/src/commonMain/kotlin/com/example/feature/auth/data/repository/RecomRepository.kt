package com.example.feature.auth.data.repository

import com.example.common.ResultExceptionHandler
import com.example.common.SnackbarManager
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
    override val snackbarManager: SnackbarManager,
) : ResultExceptionHandler {
    suspend fun getHistoryQueries(): List<QueryRecommendModel> {
        return localDataSource.searchQueries.first().map { QueryRecommendModel(it, QueryType.HISTORY) }
    }

    suspend fun deleteQueryFromHistory(query: String) {
        return localDataSource.clearQuery(query)
    }

    suspend fun getQueryRecommends(
        query: String,
    ): List<QueryRecommendModel> {
        val localQueries = localDataSource.searchQueries.first().toList()
            .map { QueryRecommendModel(it, QueryType.HISTORY) }

        val res = remoteDataSource.getPollTitles(query)

        res.onSuccess {
            return localQueries + it.results.map { QueryRecommendModel(it, QueryType.RECOMMEND) }
        }.handleException()

        return localQueries
    }

    suspend fun getPolls(query: String): Result<PollSearchResponse> {
        localDataSource.addQuery(query)
        return remoteDataSource.getPolls(query)
    }

    suspend fun getMainScreenResponse(): Result<MainScreenResponse> {
        return remoteDataSource.getMainScreenPolls()
    }
}