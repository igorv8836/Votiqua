package org.example.votiqua.utils

class UserNotFoundException(message: String? = null) : Exception(message)

class NoPermissionException(message: String? = null) : Exception(message)
class DatabaseException(message: String? = null) : Exception(message)
class RestrictiveException(message: String? = null) : Exception(message)

class IncorrectBodyException(message: String? = null) : Exception(message)