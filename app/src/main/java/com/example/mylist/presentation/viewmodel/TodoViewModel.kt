package com.example.mylist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylist.domain.model.Priority
import com.example.mylist.domain.model.Todo
import com.example.mylist.domain.usecase.AddTodoUseCase
import com.example.mylist.domain.usecase.DeleteTodoUseCase
import com.example.mylist.domain.usecase.GetTodosUseCase
import com.example.mylist.domain.usecase.SortBy
import com.example.mylist.domain.usecase.ToggleTodoUseCase
import com.example.mylist.domain.usecase.TodoFilter
import com.example.mylist.domain.usecase.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(TodoFormState())
    val formState: StateFlow<TodoFormState> = _formState.asStateFlow()

    init {
        observeTodos()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTodos() {
        _uiState
            .map { it.filter to it.sortBy }
            .distinctUntilChanged()
            .flatMapLatest { (filter, sortBy) -> getTodosUseCase(filter, sortBy) }
            .onEach { todos -> _uiState.update { it.copy(todos = todos, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun setFilter(filter: TodoFilter) {
        _uiState.update { it.copy(filter = filter, isLoading = true) }
    }

    fun setSortBy(sortBy: SortBy) {
        _uiState.update { it.copy(sortBy = sortBy) }
    }

    fun toggleComplete(id: Long) {
        viewModelScope.launch {
            toggleTodoUseCase(id).onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun deleteTodo(id: Long) {
        viewModelScope.launch {
            deleteTodoUseCase(id).onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun showAddDialog(todo: Todo? = null) {
        _formState.value = if (todo != null) {
            TodoFormState(title = todo.title, description = todo.description, priority = todo.priority)
        } else {
            TodoFormState()
        }
        _uiState.update { it.copy(showAddDialog = true, editingTodo = todo) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showAddDialog = false, editingTodo = null) }
        _formState.value = TodoFormState()
    }

    fun onTitleChange(title: String) {
        _formState.update { it.copy(title = title, titleError = null) }
    }

    fun onDescriptionChange(description: String) {
        _formState.update { it.copy(description = description) }
    }

    fun onPriorityChange(priority: Priority) {
        _formState.update { it.copy(priority = priority) }
    }

    fun saveTodo() {
        val form = _formState.value
        if (form.title.isBlank()) {
            _formState.update { it.copy(titleError = "O título é obrigatório") }
            return
        }
        viewModelScope.launch {
            val editing = _uiState.value.editingTodo
            val result = if (editing != null) {
                updateTodoUseCase(
                    editing.copy(
                        title = form.title,
                        description = form.description,
                        priority = form.priority
                    )
                )
            } else {
                addTodoUseCase(
                    Todo(title = form.title, description = form.description, priority = form.priority)
                ).map { }
            }
            result
                .onSuccess { dismissDialog() }
                .onFailure { e -> _uiState.update { it.copy(error = e.message) } }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
