package com.example.mylist.core.domain.usecase
import com.example.mylist.core.domain.model.Todo
import com.example.mylist.core.domain.repository.TodoRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(
    private val repository: TodoRepository,
) {
    suspend operator fun invoke(todo: Todo): Result<Long> = runCatching {
        require(todo.title.isNotBlank()) { "O título não pode estar em branco" }
        repository.insert(todo)
    }
}
