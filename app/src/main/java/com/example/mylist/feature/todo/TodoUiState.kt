package com.example.mylist.feature.todo
import com.example.mylist.core.domain.model.Priority
import com.example.mylist.core.domain.model.Todo
import com.example.mylist.core.domain.usecase.SortBy
import com.example.mylist.core.domain.usecase.TodoFilter

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val filter: TodoFilter = TodoFilter.ALL,
    val sortBy: SortBy = SortBy.CREATED_AT,
    val showAddDialog: Boolean = false,
    val editingTodo: Todo? = null
)

data class TodoFormState(
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val titleError: String? = null,
    val dueDateText: String = "",
    val dueDateError: String? = null
)
