package com.example.taskup.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
public class Todo (
    var item: String?,
    var description: String?,
    var priority: Int?,
    var deadline: Long?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}