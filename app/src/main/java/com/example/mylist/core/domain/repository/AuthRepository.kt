package com.example.mylist.domain.repository

import com.example.mylist.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
}
