package com.example.mylist.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mylist.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY created_at DESC")
    fun getAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE is_completed = :completed ORDER BY created_at DESC")
    fun getByCompleted(completed: Boolean): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getById(id: Long): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: TodoEntity): Long

    @Update
    suspend fun update(entity: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE todos SET is_completed = NOT is_completed WHERE id = :id")
    suspend fun toggleComplete(id: Long)
}
