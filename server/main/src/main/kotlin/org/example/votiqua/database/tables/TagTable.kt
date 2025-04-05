package org.example.votiqua.database.tables

import org.jetbrains.exposed.sql.Table

object TagTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50).uniqueIndex()
    override val primaryKey = PrimaryKey(id)
}