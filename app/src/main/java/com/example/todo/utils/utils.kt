package com.example.todo.utils

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

fun showDatePickerDialog(context: Context, callback: (String, Calendar) -> Unit) {
    val dialog = DatePickerDialog(context)
    dialog.setOnDateSetListener { datePicker, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        callback("$day/${month + 1}/$year", calendar)

    }
    dialog.show()
}

fun showTimePickerDialog(
    hour: Int,
    minute: Int,
    title: String,
    fragmentManager: FragmentManager,
    callback: (Int, Int) -> Unit
) {
    val picker =
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hour)
            .setMinute(minute)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTitleText(title)
            .build()
    picker.show(fragmentManager, "")

    picker.addOnPositiveButtonClickListener {

        callback(picker.hour, picker.minute)
    }

    picker.addOnNegativeButtonClickListener {
        picker.dismiss()
    }
    picker.addOnDismissListener {
        picker.dismiss()
    }
}

fun getHourIn12(hour: Int): Int {
    return if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
}

fun getTimeAmPm(hour: Int): String {
    return if (hour < 12) "AM" else "PM"
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}
