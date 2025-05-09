package org.example.votiqua.server.common.utils

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

val MOSCOW_ZONE_ID: ZoneId = ZoneId.of("Europe/Moscow")

fun LocalDateTime.toTimestampMoscow(): Long {
    return this.toEpochSecond(MOSCOW_ZONE_ID.rules.getOffset(this))
}

fun Long.toLocalDateTimeMoscow(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(
        this,
        0,
        MOSCOW_ZONE_ID.rules.getOffset(LocalDateTime.now(MOSCOW_ZONE_ID))
    )
}

fun currentTimestamp(): Long {
    return LocalDateTime.now(Clock.systemUTC()).second.toLong()
}

fun currentDateTime(): LocalDateTime {
    return LocalDateTime.now(Clock.systemUTC())
}

fun LocalDateTime.toUtcTimestamp(): Long {
    return this.toEpochSecond(ZoneOffset.UTC)
}

fun Long.toUtcDateTime(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)
}