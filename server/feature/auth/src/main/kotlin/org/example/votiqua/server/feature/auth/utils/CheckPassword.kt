package org.example.votiqua.server.feature.auth.utils

import org.example.votiqua.server.common.models.MainConfig

fun String.checkPasswordLength() {
    if (MainConfig.MIN_PASSWORD_LEN.value > this.length){
        throw IllegalArgumentException(MainConfig.MIN_PASSWORD_LEN.text)
    }
    if (MainConfig.MAX_PASSWORD_LEN.value < this.length){
        throw IllegalArgumentException(MainConfig.MAX_PASSWORD_LEN.text)
    }
}