package org.example.votiqua.server.feature.voting.database

import org.example.votiqua.server.feature.auth.api.database.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object VoteTable : Table() {
    val id = integer("id").autoIncrement()
    val pollId = integer("poll_id").references(PollTable.id, onDelete = ReferenceOption.CASCADE)
    val optionId = integer("option_id").references(PollOptionTable.id, onDelete = ReferenceOption.CASCADE)
    val userId = integer("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val votedAt = datetime("voted_at")
    val ipAddress = varchar("ip_address", 45)
    override val primaryKey = PrimaryKey(id)
}