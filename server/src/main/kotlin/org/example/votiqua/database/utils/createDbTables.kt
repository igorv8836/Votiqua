package org.example.votiqua.database.utils

import org.example.votiqua.database.tables.PollOptionTable
import org.example.votiqua.database.tables.PollTable
import org.example.votiqua.database.tables.UserTable
import org.example.votiqua.database.tables.VoteTable
import org.jetbrains.exposed.sql.SchemaUtils


fun createDbTables() {
    SchemaUtils.create(
        PollOptionTable,
        PollTable,
        UserTable,
        VoteTable,
    )
}