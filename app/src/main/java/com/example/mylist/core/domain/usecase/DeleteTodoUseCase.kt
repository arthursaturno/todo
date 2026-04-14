package com.example.mylist.core.domain.usecase
import com.example.mylist.core.domain.repository.TodoRepository
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val repository: TodoRepository,
) {
    suspend operator fun invoke(id: Long): Result<Unit> = runCatching {
        repository.delete(id)
    }
}
