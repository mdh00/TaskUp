package com.example.taskup.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import androidx.room.migration.Migration

@Database(entities = [Todo::class], version = 2)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_db"
                )
                    .addMigrations(migration1to2) // Add migration here
                    .build()
                    .also { INSTANCE = it }
            }
        }

        // Migration code goes here
        private val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Todo ADD COLUMN description TEXT")
                database.execSQL("ALTER TABLE Todo ADD COLUMN priority INTEGER DEFAULT 0")
                database.execSQL("ALTER TABLE Todo ADD COLUMN deadline INTEGER DEFAULT 0")
            }
        }

    }
}
