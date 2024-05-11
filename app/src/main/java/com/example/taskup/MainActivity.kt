package com.example.taskup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskup.database.Todo
import com.example.taskup.database.TodoDatabase
import com.example.taskup.database.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.view.LayoutInflater


class MainActivity : AppCompatActivity() {
    private lateinit var adapter:TodoAdapter
    private lateinit var viewModel:MainActivityData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.todoList)
        val repository = TodoRepository(TodoDatabase.getInstance(this))
        viewModel = ViewModelProvider(this)[MainActivityData::class.java]

        viewModel.data.observe(this){
            adapter = TodoAdapter(it, repository, viewModel)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTodoItems()

            runOnUiThread {
                viewModel.setData(data)
            }
        }

        val addTodo: Button = findViewById(R.id.addTodo)


        addTodo.setOnClickListener{
            displayAlert(repository)
        }

    }

    private fun displayAlert(repository: TodoRepository) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_add_todo, null)
        val builder = AlertDialog.Builder(this).apply {
            setView(dialogView)
            setTitle("Enter New Task")
            setPositiveButton("OK") { dialog, _ ->
                val itemEditText = dialogView.findViewById<EditText>(R.id.editTextTodoItem)
                val descriptionEditText = dialogView.findViewById<EditText>(R.id.editTextDescription)
                val priorityEditText = dialogView.findViewById<EditText>(R.id.editTextPriority)
                val deadlineEditText = dialogView.findViewById<EditText>(R.id.editTextDeadline)
                deadlineEditText.inputType = InputType.TYPE_CLASS_TEXT
                deadlineEditText.hint = "YYYY-MM-DD"


                val newItem = itemEditText.text.toString()
                val newDescription = descriptionEditText.text.toString()
                val newPriority = priorityEditText.text.toString().toIntOrNull()
                val newDeadline = deadlineEditText.text.toString().toLongOrNull()

                if (newItem.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.insert(Todo(newItem, newDescription, newPriority, newDeadline))
                        val data = repository.getAllTodoItems()
                        runOnUiThread {
                            viewModel.setData(data)
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Todo item cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        }
        builder.create().show()
    }


}