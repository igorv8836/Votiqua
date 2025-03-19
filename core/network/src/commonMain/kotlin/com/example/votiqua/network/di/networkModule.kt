package com.example.votiqua.network.di

import com.example.votiqua.network.getPlatformHttpClient
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

fun networkModule() = module {
    single(createdAtStart = true) {
        getReadyHttpClient()
    }
}

private fun getReadyHttpClient(): HttpClient {
    Napier.i(message = "start", tag = "startClient")
    return getPlatformHttpClient().config {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                explicitNulls = false
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 45_000
            connectTimeoutMillis = 20_000
            socketTimeoutMillis = 45_000
        }
        install(HttpRequestRetry) {
            maxRetries = 3
            retryOnExceptionIf { _, cause -> cause is IOException }
        }
        install(UserAgent) {
            agent = "Votiqua/1.0"
        }
        install(WebSockets) {
            pingInterval = 15.seconds
            maxFrameSize = Long.MAX_VALUE
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            url {
                protocol = URLProtocol.HTTPS
                host = "quickqueues.tech"
            }
        }
    }
}