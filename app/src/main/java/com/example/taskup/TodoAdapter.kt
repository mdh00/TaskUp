package com.example.taskup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.taskup.database.DateUtils
import com.example.taskup.database.Todo
import com.example.taskup.database.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoAdapter(initialItems:List<Todo>, repository: TodoRepository,
                  viewModel:MainActivityData):Adapter<TodoViewHolder>() {
    var context:Context? =null
    val items = initialItems.toMutableList()
    val repository = repository
    val viewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item,parent,false)
        context = parent.context
        return TodoViewHolder(view)
    }
    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = items[position]
        holder.descriptionTextView.text = currentItem.description ?: ""
        holder.priorityNumberDecimal.text = currentItem.priority?.toString() ?: ""
        holder.deadlineTextTime.text = DateUtils.formatDateFromLong(currentItem.deadline ?: 0L)

        holder.todoCheckBox.text = items[position].item ?: ""

        holder.ivDelete.setOnClickListener{
            val isChecked = holder.todoCheckBox.isChecked

            if(isChecked){
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(items[position])
                    val data = repository.getAllTodoItems()
                    withContext(Dispatchers.Main){
                        viewModel.setData(data)
                    }
                }
                Toast.makeText(context, "Item Deleted", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Select the item to delete", Toast.LENGTH_LONG).show()
            }

        }

        holder.ivEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditTodoActivity::class.java).apply {
                putExtra("itemId", currentItem.id)
                putExtra("item", currentItem.item)
                putExtra("description", currentItem.description)
                putExtra("priority", currentItem.priority)
                putExtra("deadline", currentItem.deadline)
            }
            context.startActivity(intent)
        }



//        holder.ivEdit.setOnClickListener {
//            // When edit button is clicked, open an alert dialog to edit the todo item
//            val context = holder.itemView.context
//            val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_add_todo, null)
//            val builder = AlertDialog.Builder(context).apply {
//                setView(dialogView)
//                setTitle("Edit your Todo")
//                // Pre-fill the fields with the current item's details
//                dialogView.findViewById<EditText>(R.id.editTextTodoItem).setText(currentItem.item)
//                dialogView.findViewById<EditText>(R.id.editTextDescription).setText(currentItem.description)
//                // Ensure priority and deadline are converted to CharSequence
//                dialogView.findViewById<EditText>(R.id.editTextPriority).setText(currentItem.priority?.toString() ?: "")
//                dialogView.findViewById<EditText>(R.id.editTextDeadline).setText(currentItem.deadline?.toString() ?: "")
//
//
//                setPositiveButton("Save") { dialog, _ ->
//                    // Get the edited values from the dialog fields
//                    val newItem = dialogView.findViewById<EditText>(R.id.editTextTodoItem).text.toString()
//                    val newDescription = dialogView.findViewById<EditText>(R.id.editTextDescription).text.toString()
//                    val newPriority = dialogView.findViewById<EditText>(R.id.editTextPriority).text.toString().toIntOrNull()
//                    val newDeadline = dialogView.findViewById<EditText>(R.id.editTextDeadline).text.toString().toLongOrNull()
//
//                    // Update the item in the database
//                    CoroutineScope(Dispatchers.IO).launch {
//                        repository.update(Todo(newItem, newDescription, newPriority, newDeadline))
//
//                        val data = repository.getAllTodoItems()
//                        Log.e("Tag", data.toString())
//                        withContext(Dispatchers.Main) {
//                            viewModel.setData(data)
//                        }
//                    }
//                }
//                setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.cancel()
//                }
//            }
//            builder.create().show()
//        }

    }

    fun setItems(newItems: List<Todo>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
        Log.d("AdapterData", "New data received: $newItems")

    }


}
