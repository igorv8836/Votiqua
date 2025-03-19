package com.example.votiqua.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java

internal actual fun getPlatformHttpClient() = HttpClient(Java)