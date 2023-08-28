package com.example.todo.utils

import android.app.DatePickerDialog
import android.content.Context
import java.util.Calendar

fun showDatePickerDialog(context: Context, callback: (String, Calendar) -> Unit) {
    val dialog = DatePickerDialog(context)
    dialog.setOnDateSetListener { datePicker, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        callback("$day/${month + 1}/$year", calendar)

    }
    dialog.show()
}

