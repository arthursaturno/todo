package com.example.mylist.feature.todo.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mylist.core.domain.model.Priority
import com.example.mylist.core.ui.components.AppTextField
import com.example.mylist.feature.todo.TodoFormState

@Composable
fun TodoFormDialog(
    isEditing: Boolean,
    formState: TodoFormState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onDueDateChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEditing) "Editar Tarefa" else "Nova Tarefa")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTextField(
                    value = formState.title,
                    onValueChange = onTitleChange,
                    label = "Título *",
                    error = formState.titleError,
                    modifier = Modifier.fillMaxWidth()
                )

                AppTextField(
                    value = formState.description,
                    onValueChange = onDescriptionChange,
                    label = "Descrição",
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                AppTextField(
                    value = formState.dueDateText,
                    onValueChange = onDueDateChange,
                    label = "Prazo (dd/MM/aaaa)",
                    error = formState.dueDateError,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Prioridade",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Priority.entries.forEach { priority ->
                        FilterChip(
                            selected = formState.priority == priority,
                            onClick = { onPriorityChange(priority) },
                            label = { Text(priority.label) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(if (isEditing) "Salvar" else "Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
