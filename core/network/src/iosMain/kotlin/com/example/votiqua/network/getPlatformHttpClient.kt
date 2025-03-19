package com.example.votiqua.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

internal actual fun getPlatformHttpClient() = HttpClient(Darwin)