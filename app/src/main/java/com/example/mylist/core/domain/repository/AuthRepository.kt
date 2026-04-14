package com.example.mylist.core.domain.repository
import com.example.mylist.core.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
}
