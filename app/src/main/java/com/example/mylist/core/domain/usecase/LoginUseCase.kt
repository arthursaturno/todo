package com.example.mylist.core.domain.usecase
import com.example.mylist.core.domain.model.User
import com.example.mylist.core.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) return Result.failure(IllegalArgumentException("E-mail obrigatório"))
        if (password.isBlank()) return Result.failure(IllegalArgumentException("Senha obrigatória"))
        if (password.length < 6) return Result.failure(IllegalArgumentException("Senha deve ter pelo menos 6 caracteres"))
        return repository.login(email, password)
    }
}
