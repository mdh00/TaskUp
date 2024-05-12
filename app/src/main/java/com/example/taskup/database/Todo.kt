package com.example.taskup.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
public class Todo (
    var item: String?,
    var description: String?,
    var priority: Int?,
    var deadline: Long?,
    //var date: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(id: Int?, item: String?, description: String?, priority: Int?, deadline: Long?) : this(item, description, priority, deadline) {
        this.id = id
    }

    override fun toString(): String {
        return "Todo(id=$id, item=$item, description=$description, priority=$priority, deadline=$deadline)"
    }

//    companion object {
//        fun formatDateFromLong(timeInMillis: Long): String {
//            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            val date = Date(timeInMillis)
//            return sdf.format(date)
//        }
//    }
}
