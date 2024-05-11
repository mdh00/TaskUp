package com.example.taskup

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class TodoViewHolder (view: View): ViewHolder(view){
    val todoCheckBox: CheckBox = view.findViewById(R.id.todoCheckBox)
    val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
    val deadlineTextTime: TextView = view.findViewById(R.id.deadlineTextTime)
    val priorityNumberDecimal: TextView = view.findViewById(R.id.priorityNumberDecimal)
    val ivEdit: ImageView = view.findViewById(R.id.ivEdit)
    val ivDelete: ImageView = view.findViewById(R.id.ivDelete)

}