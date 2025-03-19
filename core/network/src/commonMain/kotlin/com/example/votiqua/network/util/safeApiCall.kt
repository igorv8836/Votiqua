package com.example.votiqua.network.util

import com.example.votiqua.network.model.NetworkException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException

suspend inline fun <reified T> safeApiCall(apiCall: () -> HttpResponse): Result<T> {
    return try {
        val response = apiCall()
        when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.body<T>()
                Result.success(responseBody)
            }
            HttpStatusCode.BadRequest -> {
                Result.failure(NetworkException.ClientErrorException(response.bodyAsText()))
            }
            HttpStatusCode.Unauthorized -> {
                Result.failure(NetworkException.Unauthorized("UnAuthorized: " + response.bodyAsText()))
            }
            HttpStatusCode.Conflict -> {
                Result.failure(NetworkException.ClientErrorException("Conflict: " + response.bodyAsText()))
            }
            HttpStatusCode.TooManyRequests -> {
                Result.failure(NetworkException.ClientErrorException(response.bodyAsText()))
            }
            else -> {
                Result.failure(NetworkException.UnexpectedException("Unexpected status code: ${response.status.value}, ${response.bodyAsText()}"))
            }
        }
    } catch (e: ClientRequestException) {
        Result.failure(NetworkException.ClientErrorException(e.response.bodyAsText()))
    } catch (e: ServerResponseException) {
        Result.failure(NetworkException.ServerErrorException(e.response.bodyAsText()))
    } catch (e: IOException) {
        Result.failure(NetworkException.NetworkIOException("${e.message}"))
    } catch (e: Exception) {
        Result.failure(NetworkException.UnexpectedException("${e.message}"))
    }
}