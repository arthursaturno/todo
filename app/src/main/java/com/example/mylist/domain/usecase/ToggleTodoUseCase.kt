package com.example.mylist.domain.usecase

import com.example.mylist.domain.repository.TodoRepository
import javax.inject.Inject

class ToggleTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = runCatching {
        repository.toggleComplete(id)
    }
}
