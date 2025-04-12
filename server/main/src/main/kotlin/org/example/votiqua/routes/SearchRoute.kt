package org.example.votiqua.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.example.votiqua.database.tables.SearchHistoryTable
import org.example.votiqua.models.search.PollSearchResponse
import org.example.votiqua.models.search.PollSearchResult
import org.example.votiqua.repositories.ProfileRepository
import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.common.utils.getUserId
import org.example.votiqua.server.common.utils.handleBadRequest
import org.example.votiqua.server.feature.voting.data.repository.PollRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

fun Route.searchRoute() {
    val pollRepository by application.inject<PollRepository>()

    route("search") {
        get {
            val query = call.request.queryParameters["query"] ?: run {
                call.handleBadRequest()
                return@get
            }
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

            if (limit > 100) {
                call.handleBadRequest("Limit cannot be greater than 100")
                return@get
            }

            val pollTitles = pollRepository.searchPollTitles(query, limit)
            call.respond(HttpStatusCode.OK, PollSearchResponse(query, pollTitles.size, pollTitles))
        }

        get("/polls") {
            val query = call.request.queryParameters["query"] ?: run {
                call.handleBadRequest()
                return@get
            }
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

            if (limit > 100) {
                call.handleBadRequest("Limit cannot be greater than 100")
                return@get
            }

            val userId = call.getUserId()

            // Сохраняем историю поиска для авторизованных пользователей
            if (userId != null) {
                transaction {
                    SearchHistoryTable.insert {
                        it[SearchHistoryTable.userId] = userId
                        it[SearchHistoryTable.query] = query
                        it[createdAt] = currentDateTime()
                    }
                }
            }

            val polls = pollRepository.searchPolls(query, limit)
            call.respond(HttpStatusCode.OK, PollSearchResult(query, polls.size, polls))
        }

        get("/users") {
            val query = call.request.queryParameters["query"] ?: run {
                call.handleBadRequest()
                return@get
            }
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

            if (limit > 100) {
                call.handleBadRequest("Limit cannot be greater than 100")
                return@get
            }

            val users = ProfileRepository.searchUsers(query, limit)
            call.respond(HttpStatusCode.OK, mapOf("query" to query, "count" to users.size, "results" to users))
        }
    }
}
