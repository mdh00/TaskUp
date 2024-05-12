package com.example.taskup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.taskup.database.DateUtils
import com.example.taskup.database.Todo
import com.example.taskup.database.TodoDatabase
import com.example.taskup.database.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditTodoActivity : AppCompatActivity() {
    private lateinit var adapter: TodoAdapter
    private lateinit var viewModel: MainActivityData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        val textViewHeading = findViewById<TextView>(R.id.textViewHeading)

        textViewHeading.text = "Edit Todo"

        findViewById<TextView>(R.id.saveBtn).visibility = View.GONE

        adapter = TodoAdapter(emptyList(), TodoRepository(TodoDatabase.getInstance(this)), MainActivityData())
        viewModel = ViewModelProvider(this).get(MainActivityData::class.java)

        // Retrieve data from intent extras
        val itemId = intent.getIntExtra("itemId", -1)
        val item = intent.getStringExtra("item")
        val description = intent.getStringExtra("description")
        val priority = intent.getIntExtra("priority", 0)
        val deadline = intent.getLongExtra("deadline", 0L)

        // Pre-fill edit fields with existing data
        findViewById<EditText>(R.id.editTextTodoItem).setText(item)
        findViewById<EditText>(R.id.editTextDescription).setText(description)
        findViewById<EditText>(R.id.editTextPriority).setText(priority.toString())
        findViewById<EditText>(R.id.editTextDeadline).setText(deadline.toString())


        findViewById<Button>(R.id.okayBtn).setOnClickListener {
            // Get the updated data from the edit fields
            val newItem = findViewById<EditText>(R.id.editTextTodoItem).text.toString()
            val newDescription = findViewById<EditText>(R.id.editTextDescription).text.toString()
            val newPriority =
                findViewById<EditText>(R.id.editTextPriority).text.toString().toIntOrNull() ?: 0
            val formattedDeadline = DateUtils.formatDateFromLong(deadline)
            findViewById<EditText>(R.id.editTextDeadline).setText(formattedDeadline)
            val newDeadline =
                findViewById<EditText>(R.id.editTextDeadline).text.toString().toLongOrNull() ?: 0L

            // Update the todo in the database
            updateTodoInDatabase(itemId, newItem, newDescription, newPriority, newDeadline)

            // Finish the activity
            finish()
        }
    }

    private fun updateTodoInDatabase(
        itemId: Int,
        newItem: String,
        newDescription: String,
        newPriority: Int,
        newDeadline: Long
    ) {
        // Create a repository instance
        val repository = TodoRepository(TodoDatabase.getInstance(this))

        Log.d("EditTodoActivity", "Before Update: newItem=$newItem, newDescription=$newDescription, newPriority=$newPriority, newDeadline=$newDeadline")

        // Update the todo item in the database
        CoroutineScope(Dispatchers.IO).launch {
            // Update the todo item in the database
            repository.update(Todo(itemId,newItem, newDescription, newPriority, newDeadline))


            // Fetch the updated list of todo items from the database
            val updatedData = repository.getAllTodoItems()

            Log.d("EditTodoActivity", "After Update: updatedData=$updatedData")

            // Update the UI with the updated data
            withContext(Dispatchers.Main) {
                // Assuming you have a reference to your adapter and viewModel
                adapter.setItems(updatedData)
                Log.d("UI update data", "After Update: updatedData=$updatedData")
            }

        }
    }
}