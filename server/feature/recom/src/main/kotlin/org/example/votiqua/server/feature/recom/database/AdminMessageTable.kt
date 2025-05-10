package org.example.votiqua.server.feature.recom.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object AdminMessageTable : Table() {
    val id = integer("id").autoIncrement()
    val message = text("message")
    val isActive = bool("is_active").default(true)
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
} 