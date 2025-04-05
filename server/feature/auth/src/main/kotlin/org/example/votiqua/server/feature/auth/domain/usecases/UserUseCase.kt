package org.example.votiqua.server.feature.auth.domain.usecases

import org.example.votiqua.models.auth.LoginRequest
import org.example.votiqua.models.auth.RegisterRequest
import org.example.votiqua.models.common.ErrorType
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.HTTPUnauthorizedException
import org.example.votiqua.server.common.models.MainConfig
import org.example.votiqua.server.common.models.OutOfConfigRangeException
import org.example.votiqua.server.common.models.auth.UserModel
import org.example.votiqua.server.feature.auth.data.UserRepository
import org.example.votiqua.server.feature.auth.utils.HashFactory
import org.example.votiqua.server.feature.auth.utils.checkPasswordLength


class UserUseCase(
    private val userRepository: UserRepository,
    private val hashFactory: HashFactory,
    private val jwtUseCase: JwtUseCase,
) {
    suspend fun findUserByEmail(email: String): UserModel? = userRepository.getUserByEmail(email)

    suspend fun createUser(
        user: RegisterRequest,
    ): String {

        if (MainConfig.MIN_NICKNAME_LEN.value > user.username.length) {
            throw OutOfConfigRangeException(MainConfig.MIN_NICKNAME_LEN.text)
        }
        if (MainConfig.MAX_NICKNAME_LEN.value < user.username.length) {
            throw OutOfConfigRangeException(MainConfig.MAX_NICKNAME_LEN.text)
        }

        user.password.checkPasswordLength()

        if (user.email.length <= 3 || !user.email.contains("@")) {
            throw HTTPConflictException(ErrorType.INVALID_EMAIL.message)
        }

        val passwordHash = hashFactory.hash(user.password)

        userRepository.insertUser(
            userModel = UserModel(
                email = user.email,
                passwordHash = passwordHash,
                username = user.username,
            )
        )

        val registeredUser = checkUser(user.email, passwordHash) ?: throw Exception("ERROR")
        return jwtUseCase.generateToken(registeredUser)
    }

    suspend fun loginUser(
        emailRequest: LoginRequest,
    ): String {
        val hashedPassword = hashFactory.hash(emailRequest.password)
        val user = checkUser(emailRequest.email, hashedPassword)
            ?: throw HTTPUnauthorizedException(ErrorType.INVALID_CREDENTIALS.message)

        return jwtUseCase.generateToken(user)
    }

    suspend fun isUsernameTaken(username: String): Boolean = userRepository.isUsernameTaken(username)

    suspend fun checkUser(email: String, password: String): UserModel? = userRepository.checkUser(email, password)

    suspend fun updatePassword(email: String, password: String) = userRepository.updatePassword(email, password)

    suspend fun deleteUser(email: String, password: String) = userRepository.deleteUser(email, password)
}
