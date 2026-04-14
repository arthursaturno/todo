package com.example.mylist.feature.todo

import com.example.mylist.domain.model.Priority
import com.example.mylist.domain.model.Todo
import com.example.mylist.domain.usecase.SortBy
import com.example.mylist.domain.usecase.TodoFilter

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
    val titleError: String? = null
)
