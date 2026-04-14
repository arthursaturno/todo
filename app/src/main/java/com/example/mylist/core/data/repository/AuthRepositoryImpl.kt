package com.example.mylist.core.data.repository
import com.example.mylist.core.domain.model.User
import com.example.mylist.core.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> = runCatching {
        User(email = email, name = email.substringBefore("@"))
    }
}
