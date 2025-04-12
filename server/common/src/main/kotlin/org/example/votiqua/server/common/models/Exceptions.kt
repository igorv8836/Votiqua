package org.example.votiqua.server.common.models

class UserNotFoundException(message: String? = null) : Exception(message)

class NoPermissionException(message: String? = null) : Exception(message)
class DatabaseException(message: String? = null) : Exception(message)
class RestrictiveException(message: String? = null) : Exception(message)

class HTTPConflictException(message: String? = null) : Exception(message)
class HTTPUnauthorizedException(message: String? = null) : Exception(message)
class HTTPForbiddenException(message: String? = null) : Exception(message)

class IncorrectBodyException(message: String? = null) : Exception(message)
class OutOfConfigRangeException(message: String? = null) : Exception(message)