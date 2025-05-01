package com.example.votiqua.network.util

import com.example.votiqua.network.model.NetworkException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import org.example.votiqua.models.common.BaseResponse

suspend inline fun <reified T> safeApiCall(apiCall: () -> HttpResponse): Result<T> {
    return try {
        val response = apiCall()
        when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.body<T>()
                Result.success(responseBody)
            }
            HttpStatusCode.BadRequest -> {
                val errorText = response.tryGetErrorText() ?: ("BadRequest: " + response.bodyAsText())
                Result.failure(NetworkException.ClientErrorException(errorText))
            }
            HttpStatusCode.Unauthorized -> {
                val errorText = response.tryGetErrorText() ?: ("Unauthorized: " + response.bodyAsText())
                return Result.failure(NetworkException.Unauthorized(errorText))
            }
            HttpStatusCode.Conflict -> {
                val errorText = response.tryGetErrorText() ?: ("Conflict: " + response.bodyAsText())
                Result.failure(NetworkException.ClientErrorException(errorText))
            }
            HttpStatusCode.TooManyRequests -> {
                Result.failure(NetworkException.ClientErrorException(response.bodyAsText()))
            }
            else -> {
                val errorText = response.tryGetErrorText()
                    ?: "Unexpected status code: ${response.status.value}, ${response.bodyAsText()}"

                Result.failure(NetworkException.UnexpectedException(errorText))
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

suspend fun HttpResponse.tryGetErrorText(): String? {
    return try {
        body<BaseResponse<String>>().message
    } catch (e: Exception) {
        null
    }
}