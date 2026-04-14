package com.example.mylist.feature.todo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylist.core.domain.model.Priority
import com.example.mylist.core.domain.model.Todo
import com.example.mylist.core.domain.usecase.AddTodoUseCase
import com.example.mylist.core.domain.usecase.DeleteTodoUseCase
import com.example.mylist.core.domain.usecase.GetTodosUseCase
import com.example.mylist.core.domain.usecase.SortBy
import com.example.mylist.core.domain.usecase.ToggleTodoUseCase
import com.example.mylist.core.domain.usecase.TodoFilter
import com.example.mylist.core.domain.usecase.UpdateTodoUseCase
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
import com.example.mylist.core.utils.formatDateInput
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase
) : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

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
                _uiState.update { it.copy(error = e.message ?: "Erro ao atualizar tarefa") }
            }
        }
    }

    fun deleteTodo(id: Long) {
        viewModelScope.launch {
            deleteTodoUseCase(id).onFailure { e ->
                _uiState.update { it.copy(error = e.message ?: "Erro ao deletar tarefa") }
            }
        }
    }

    fun showAddDialog(todo: Todo? = null) {
        _formState.value = if (todo != null) {
            TodoFormState(
                title = todo.title,
                description = todo.description,
                priority = todo.priority,
                dueDateText = todo.dueDate?.format(dateFormatter) ?: ""
            )
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

    fun onDueDateChange(date: String) {
        _formState.update { it.copy(dueDateText = formatDateInput(date), dueDateError = null) }
    }

    fun saveTodo() {
        val form = _formState.value
        if (form.title.isBlank()) {
            _formState.update { it.copy(titleError = "O título é obrigatório") }
            return
        }

        val dueDate = if (form.dueDateText.isNotBlank()) {
            try {
                LocalDate.parse(form.dueDateText, dateFormatter).atStartOfDay()
            } catch (e: DateTimeParseException) {
                _formState.update { it.copy(dueDateError = "Formato inválido: dd/MM/aaaa") }
                return
            }
        } else null

        viewModelScope.launch {
            val editing = _uiState.value.editingTodo
            val result = if (editing != null) {
                updateTodoUseCase(
                    editing.copy(
                        title = form.title,
                        description = form.description,
                        priority = form.priority,
                        dueDate = dueDate
                    )
                )
            } else {
                addTodoUseCase(
                    Todo(
                        title = form.title,
                        description = form.description,
                        priority = form.priority,
                        dueDate = dueDate
                    )
                ).map { }
            }
            result
                .onSuccess { dismissDialog() }
                .onFailure { e -> _uiState.update { it.copy(error = e.message ?: "Erro ao salvar tarefa") } }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
