package org.example.votiqua.database.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object VoteTable : Table() {
    val id = integer("id").autoIncrement()
    val pollId = integer("poll_id").references(PollTable.id, onDelete = ReferenceOption.CASCADE)
    val optionId = integer("option_id").references(PollOptionTable.id, onDelete = ReferenceOption.CASCADE)
    val userId = integer("user_id").references(UserTable.id, onDelete = ReferenceOption.SET_NULL)
    val votedAt = datetime("voted_at")
    val ipAddress = varchar("ip_address", 45) // для хранения IPv4 и IPv6
    override val primaryKey = PrimaryKey(id)
}