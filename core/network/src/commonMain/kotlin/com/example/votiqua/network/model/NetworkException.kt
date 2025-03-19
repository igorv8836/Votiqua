package com.example.votiqua.network.model

sealed class NetworkException(message: String) : Exception(message) {
    class ClientErrorException(message: String) : NetworkException(message)
    class ServerErrorException(message: String) : NetworkException(message)
    class NetworkIOException(message: String) : NetworkException(message)
    class Unauthorized(message: String) : NetworkException(message)
    class UnexpectedException(message: String) : NetworkException(message)
}