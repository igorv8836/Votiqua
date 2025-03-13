package org.example.votiqua.data.repository

import org.example.votiqua.domain.model.user.User
import org.example.votiqua.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    var saveUser: User? = null
    override fun getUser(): User {
        if (saveUser == null) {
            saveUser = User(
                email = "email@mail.ru",
                nickname = "master",
                photoUrl = "https://avatars.mds.yandex.net/i?id=9785f59e5d941e55882930681a09a53932226e63-11376477-images-thumbs&n=13",
            )
        }
        return saveUser!!
    }

    override fun updateNickname(newNickname: String) {
        saveUser = saveUser?.copy(nickname = newNickname)
    }

    override fun updatePhoto(uri: String) {
        saveUser = saveUser?.copy(photoUrl = uri)
    }
}