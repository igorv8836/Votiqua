package org.example.votiqua.domain.repository

import com.example.common.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode): Result<Unit>
}