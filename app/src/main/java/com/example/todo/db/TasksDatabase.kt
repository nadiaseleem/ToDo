package com.example.todo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.dao.TasksDao
import com.example.todo.model.Task

@Database(entities = [Task::class], version = 2, exportSchema = true)

abstract class TasksDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao

    companion object {
        private var instance: TasksDatabase? = null

        fun getInstance(context: Context): TasksDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, TasksDatabase::class.java, "tasksDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }

}