package com.example.taskup.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(todo:Todo)

    @Query("SELECT * FROM Todo")
    fun getAllToDo():List<Todo>

    @Query("SELECT * FROM Todo WHERE id=:id")
    fun getTodo(id:Int):Todo

    @Delete
    suspend fun delete(todo:Todo)

    @Update
    suspend fun update(todo: Todo)

}