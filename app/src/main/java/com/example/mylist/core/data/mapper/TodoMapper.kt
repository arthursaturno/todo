package com.example.mylist.data.mapper

import com.example.mylist.data.local.entity.TodoEntity
import com.example.mylist.domain.model.Priority
import com.example.mylist.domain.model.Todo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

fun TodoEntity.toDomain(): Todo = Todo(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    priority = Priority.valueOf(priority),
    createdAt = LocalDateTime.parse(createdAt, formatter),
    dueDate = dueDate?.let { LocalDateTime.parse(it, formatter) }
)

fun Todo.toEntity(): TodoEntity = TodoEntity(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    priority = priority.name,
    createdAt = createdAt.format(formatter),
    dueDate = dueDate?.format(formatter)
)
