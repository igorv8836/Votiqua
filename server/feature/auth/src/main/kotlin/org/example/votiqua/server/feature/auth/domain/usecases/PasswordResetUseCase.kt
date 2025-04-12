package org.example.votiqua.server.feature.auth.domain.usecases

import org.example.votiqua.models.auth.PasswordResetRequest
import org.example.votiqua.models.common.ErrorType
import org.example.votiqua.server.common.models.HTTPConflictException
import org.example.votiqua.server.common.models.MainConfig
import org.example.votiqua.server.common.models.UserNotFoundException
import org.example.votiqua.server.common.utils.currentDateTime
import org.example.votiqua.server.common.utils.toUtcTimestamp
import org.example.votiqua.server.feature.auth.data.EmailService
import org.example.votiqua.server.feature.auth.data.PasswordResetRepository
import org.example.votiqua.server.feature.auth.utils.HashFactory
import org.example.votiqua.server.feature.auth.utils.checkPasswordLength


class PasswordResetUseCase(
    private val passwordResetRepository: PasswordResetRepository,
    private val userUseCase: UserUseCase,
    private val emailService: EmailService,
    private val hashFactory: HashFactory,
) {
    suspend fun saveResetCode(email: String): Result<Unit> {
        userUseCase.findUserByEmail(email) ?: throw UserNotFoundException(ErrorType.USER_NOT_FOUND.message)
        val code = passwordResetRepository.saveResetCode(email)
        return emailService.sendMessage(email, subject = "Votiqua password reset", message = "Password reset code: $code")
    }

    suspend fun updatePassword(request: PasswordResetRequest): Boolean {
        val existingRecord = passwordResetRepository.getResetRecord(request.email) ?: return false

        request.newPassword.checkPasswordLength()

        if (existingRecord.createdAt <= currentDateTime().minusMinutes(MainConfig.PASSWORD_RESET_TIMEOUT_MINUTES.value.toLong()).toUtcTimestamp()){
            throw HTTPConflictException(MainConfig.PASSWORD_RESET_TIMEOUT_MINUTES.text)
        }
        if (existingRecord.countInputAttempts >= MainConfig.MAX_RESET_ATTEMPTS.value){
            throw HTTPConflictException(MainConfig.MAX_RESET_ATTEMPTS.text)
        } else {
            passwordResetRepository.incrementInputAttempts(request.email)
        }
        if (existingRecord.isUsed){
            throw HTTPConflictException("This code was used")
        }

        val passwordHash = hashFactory.hash(request.newPassword)

        if (existingRecord.code == request.code) {
            passwordResetRepository.markAsUsed(request.email)
            userUseCase.updatePassword(request.email, passwordHash)
        }
        return existingRecord.code == request.code
    }
}