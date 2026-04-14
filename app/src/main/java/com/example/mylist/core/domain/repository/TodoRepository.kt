package com.example.mylist.core.domain.repository
import com.example.mylist.core.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAll(): Flow<List<Todo>>
    fun getByCompleted(completed: Boolean): Flow<List<Todo>>
    suspend fun getById(id: Long): Todo?
    suspend fun insert(todo: Todo): Long
    suspend fun update(todo: Todo)
    suspend fun delete(id: Long)
    suspend fun toggleComplete(id: Long)
}
