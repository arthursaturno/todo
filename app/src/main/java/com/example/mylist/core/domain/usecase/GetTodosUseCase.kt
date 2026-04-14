package com.example.mylist.core.domain.usecase
import com.example.mylist.core.domain.model.Todo
import com.example.mylist.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(
        filter: TodoFilter = TodoFilter.ALL,
        sortBy: SortBy = SortBy.CREATED_AT
    ): Flow<List<Todo>> {
        val source = when (filter) {
            TodoFilter.ALL -> repository.getAll()
            TodoFilter.ACTIVE -> repository.getByCompleted(completed = false)
            TodoFilter.COMPLETED -> repository.getByCompleted(completed = true)
        }
        return source.map { todos ->
            when (sortBy) {
                SortBy.CREATED_AT -> todos.sortedByDescending { it.createdAt }
                SortBy.PRIORITY -> todos.sortedBy { it.priority.order }
                SortBy.DUE_DATE -> todos.sortedWith(compareBy(nullsLast()) { it.dueDate })
            }
        }
    }
}
