package org.example.votiqua.domain.repository

import org.example.votiqua.domain.model.user.User

interface UserRepository {
    fun getUser(): User
    fun updateNickname(newNickname: String)
    fun updatePhoto(uri: String)
}