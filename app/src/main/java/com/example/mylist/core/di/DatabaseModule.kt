package com.example.mylist.core.di
import android.content.Context
import androidx.room.Room
import com.example.mylist.core.data.local.dao.TodoDao
import com.example.mylist.core.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()

    @Provides
    fun provideTodoDao(database: AppDatabase): TodoDao = database.todoDao()
}
