package com.example.taskup.database

// DateUtils.kt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatDateFromLong(timeInMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(timeInMillis)
        return sdf.format(date)
    }
}
