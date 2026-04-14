package com.example.mylist.presentation.ui.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.mylist.domain.usecase.TodoFilter


@Composable
fun FilterTabRow(
    selectedFilter: TodoFilter,
    onFilterSelected: (TodoFilter) -> Unit
) {
    val filters = TodoFilter.entries.toTypedArray()
    TabRow(selectedTabIndex = filters.indexOf(selectedFilter)) {
        filters.forEach { filter ->
            Tab(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                text = {
                    Text(
                        when (filter) {
                            TodoFilter.ALL -> "Todas"
                            TodoFilter.ACTIVE -> "Ativas"
                            TodoFilter.COMPLETED -> "Concluídas"
                        }
                    )
                }
            )
        }
    }
}
