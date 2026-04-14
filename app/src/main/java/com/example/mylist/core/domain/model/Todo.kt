package com.example.mylist.core.domain.model
import com.example.mylist.core.domain.model.Priority
import java.time.LocalDateTime

data class Todo(
    val id: Long = 0L,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val dueDate: LocalDateTime? = null
)
