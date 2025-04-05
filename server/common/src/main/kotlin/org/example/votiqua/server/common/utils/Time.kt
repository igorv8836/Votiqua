package org.example.votiqua.server.common.utils

import java.time.LocalDateTime
import java.time.ZoneId

val MOSCOW_ZONE_ID: ZoneId = ZoneId.of("Europe/Moscow")

fun LocalDateTime.toTimestampMoscow(): Long {
    return this.toEpochSecond(MOSCOW_ZONE_ID.rules.getOffset(this))
}

fun Long.toLocalDateTimeMoscow(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(this, 0, MOSCOW_ZONE_ID.rules.getOffset(LocalDateTime.now(MOSCOW_ZONE_ID)))
}