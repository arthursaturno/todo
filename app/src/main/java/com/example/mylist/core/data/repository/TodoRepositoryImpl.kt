package com.example.mylist.core.data.repository
import com.example.mylist.core.data.local.dao.TodoDao
import com.example.mylist.core.data.mapper.toDomain
import com.example.mylist.core.data.mapper.toEntity
import com.example.mylist.core.domain.model.Todo
import com.example.mylist.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {

    override fun getAll(): Flow<List<Todo>> =
        dao.getAll().map { it.map { entity -> entity.toDomain() } }

    override fun getByCompleted(completed: Boolean): Flow<List<Todo>> =
        dao.getByCompleted(completed).map { it.map { entity -> entity.toDomain() } }

    override suspend fun getById(id: Long): Todo? =
        dao.getById(id)?.toDomain()

    override suspend fun insert(todo: Todo): Long =
        dao.insert(todo.toEntity())

    override suspend fun update(todo: Todo) =
        dao.update(todo.toEntity())

    override suspend fun delete(id: Long) =
        dao.delete(id)

    override suspend fun toggleComplete(id: Long) =
        dao.toggleComplete(id)
}
