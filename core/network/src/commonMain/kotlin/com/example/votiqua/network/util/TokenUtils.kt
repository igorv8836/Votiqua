package com.example.votiqua.network.util

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

fun HttpClient.addAuthToken(token: String) {
    this.config {
        install(DefaultRequest) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}