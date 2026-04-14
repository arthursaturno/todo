package com.example.mylist.feature.todo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mylist.core.domain.usecase.SortBy
import com.example.mylist.core.domain.usecase.TodoFilter
import com.example.mylist.feature.todo.components.AppDrawer
import com.example.mylist.feature.todo.components.EmptyState
import com.example.mylist.feature.todo.components.TodoFormDialog
import com.example.mylist.feature.todo.components.TodoItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    onLogout: () -> Unit,
    viewModel: TodoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSortMenu by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                selectedFilter = uiState.filter,
                onFilterSelected = { filter ->
                    viewModel.setFilter(filter)
                    scope.launch { drawerState.close() }
                },
                onLogout = onLogout
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            when (uiState.filter) {
                                TodoFilter.ALL -> "Todas"
                                TodoFilter.ACTIVE -> "Ativas"
                                TodoFilter.COMPLETED -> "Concluídas"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    actions = {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Ordenar")
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Por data de criação") },
                                onClick = { viewModel.setSortBy(SortBy.CREATED_AT); showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Por prioridade") },
                                onClick = { viewModel.setSortBy(SortBy.PRIORITY); showSortMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Por prazo") },
                                onClick = { viewModel.setSortBy(SortBy.DUE_DATE); showSortMenu = false }
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.showAddDialog() }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar tarefa")
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                when {
                    uiState.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    uiState.todos.isEmpty() -> {
                        EmptyState(modifier = Modifier.fillMaxSize())
                    }
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(items = uiState.todos, key = { it.id }) { todo ->
                                TodoItem(
                                    todo = todo,
                                    onToggle = { viewModel.toggleComplete(todo.id) },
                                    onEdit = { viewModel.showAddDialog(todo) },
                                    onDelete = { viewModel.deleteTodo(todo.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (uiState.showAddDialog) {
            TodoFormDialog(
                isEditing = uiState.editingTodo != null,
                formState = formState,
                onTitleChange = viewModel::onTitleChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                onPriorityChange = viewModel::onPriorityChange,
                onDueDateChange = viewModel::onDueDateChange,
                onConfirm = viewModel::saveTodo,
                onDismiss = viewModel::dismissDialog
            )
        }
    }
}
