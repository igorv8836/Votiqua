package org.example.votiqua.database.utils

import org.example.votiqua.database.tables.PollOptionTable
import org.example.votiqua.database.tables.PollTable
import org.example.votiqua.database.tables.VoteTable
import org.example.votiqua.server.feature.auth.database.PasswordResetTable
import org.example.votiqua.server.feature.auth.database.UserTable
import org.jetbrains.exposed.sql.SchemaUtils


fun createDbTables() {
    SchemaUtils.create(
        PollOptionTable,
        PollTable,
        UserTable,
        VoteTable,
        PasswordResetTable,
    )
}