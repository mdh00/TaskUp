package com.example.taskup

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskup.database.Todo
import com.example.taskup.database.TodoDatabase
import com.example.taskup.database.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddTodoActivity : AppCompatActivity() {

    private lateinit var editTextDeadline: EditText
    private lateinit var viewModel: MainActivityData
    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        val textViewHeading = findViewById<TextView>(R.id.textViewHeading)
        val itemEditText = findViewById<EditText>(R.id.editTextTodoItem)
        val descriptionEditText = findViewById<EditText>(R.id.editTextDescription)
        val priorityEditText = findViewById<EditText>(R.id.editTextPriority)
        editTextDeadline = findViewById(R.id.editTextDeadline) // Initialize class-level variable
        val saveBtn: Button = findViewById(R.id.saveBtn)


        val repository = TodoRepository(TodoDatabase.getInstance(this))
        viewModel = ViewModelProvider(this).get(MainActivityData::class.java)

        adapter = TodoAdapter(emptyList(), repository, viewModel)
        viewModel.data.observe(this) { newData ->
            // Update the UI with the new data
            adapter.setItems(newData)
            Toast.makeText(this@AddTodoActivity, "Todo item added", Toast.LENGTH_SHORT).show()
        }

        // Set the text accordingly
        textViewHeading.text = "Add new Todo"
        findViewById<TextView>(R.id.okayBtn).visibility = View.GONE

        // Set OnClickListener to show DatePickerDialog when editTextDeadline is clicked
        editTextDeadline.setOnClickListener {
            showDatePickerDialog()
        }

        saveBtn.setOnClickListener {
            // Get the values from EditText fields
            val newItem = itemEditText.text.toString()
            val newDescription = descriptionEditText.text.toString()
            val newPriority = priorityEditText.text.toString().toIntOrNull()
            val newDeadlineText = editTextDeadline.text.toString()
            val newDeadline = newDeadlineText.toLongOrNull() ?: 0L

            // Check if newItem is not blank
            if (newItem.isNotBlank()) {
                // Insert the new todo item into the database
                val repository = TodoRepository(TodoDatabase.getInstance(this))
                CoroutineScope(Dispatchers.IO).launch {
                    repository.insert(Todo(newItem, newDescription, newPriority, newDeadline))
                    // Retrieve updated data after insertion
                    val data = repository.getAllTodoItems()
                    withContext(Dispatchers.Main) {
                        // Update the UI with the updated data
                        viewModel.setData(data) // Notify the ViewModel about the new data
                        adapter.setItems(data)
                        adapter.notifyDataSetChanged() // Refresh the UI
                        Toast.makeText(this@AddTodoActivity, "Todo item added", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                // Show a toast if newItem is blank
                Toast.makeText(this@AddTodoActivity, "Todo item cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog and set listener for date selection
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date as "yyyy-MM-dd"
                val formattedDate = String.format(
                    Locale.getDefault(),
                    "%d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )

                // Set the formatted date to editTextDeadline
                editTextDeadline.setText(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }
}
