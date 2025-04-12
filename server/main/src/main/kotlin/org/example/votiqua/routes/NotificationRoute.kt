package org.example.votiqua.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.votiqua.database.tables.NotificationTable
import org.example.votiqua.server.common.utils.requireAuthorization
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Route.notificationRoute() {
    route("/notifications") {
        // Получить уведомления пользователя
        get {
            val userId = call.requireAuthorization()
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
            
            val notifications = transaction {
                NotificationTable
                    .select { NotificationTable.userId eq userId }
                    .orderBy(NotificationTable.createdAt, SortOrder.DESC)
                    .limit(limit, offset.toLong())
                    .map {
                        mapOf(
                            "id" to it[NotificationTable.id],
                            "message" to it[NotificationTable.message],
                            "type" to it[NotificationTable.type],
                            "isRead" to it[NotificationTable.isRead],
                            "createdAt" to it[NotificationTable.createdAt]
                        )
                    }
            }
            
            call.respond(HttpStatusCode.OK, mapOf("count" to notifications.size, "results" to notifications))
        }
        
        // Отметить уведомление как прочитанное
        post("/{id}/read") {
            val userId = call.requireAuthorization()
            val notificationId = call.parameters["id"]?.toIntOrNull() ?: run {
                call.respond(HttpStatusCode.BadRequest, "Invalid notification ID")
                return@post
            }
            
            val updated = transaction {
                NotificationTable.update({
                    (NotificationTable.id eq notificationId) and (NotificationTable.userId eq userId)
                }) {
                    it[isRead] = true
                }
            }
            
            if (updated > 0) {
                call.respond(HttpStatusCode.OK, mapOf("success" to true))
            } else {
                call.respond(HttpStatusCode.NotFound, "Notification not found")
            }
        }
        
        // Отметить все уведомления как прочитанные
        post("/read-all") {
            val userId = call.requireAuthorization()
            
            transaction {
                NotificationTable.update({
                    (NotificationTable.userId eq userId) and (NotificationTable.isRead eq false)
                }) {
                    it[isRead] = true
                }
            }
            
            call.respond(HttpStatusCode.OK, mapOf("success" to true))
        }
    }
}