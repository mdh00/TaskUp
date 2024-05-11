package com.example.taskup.database;

class TodoRepository(
    private val db: TodoDatabase
) {
    suspend fun insert(todo: Todo) = db.getTodoDao().insert(todo)
    suspend fun delete(todo: Todo) = db.getTodoDao().delete(todo)
    fun getAllTodoItems(): List<Todo> = db.getTodoDao().getAllToDo()

    suspend fun update(todo: Todo)  = db.getTodoDao().update(todo)
}


