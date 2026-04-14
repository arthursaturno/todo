package com.example.mylist.core.data.local.database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mylist.core.data.local.dao.TodoDao
import com.example.mylist.core.data.local.entity.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        const val DATABASE_NAME = "todo_db"
    }
}
