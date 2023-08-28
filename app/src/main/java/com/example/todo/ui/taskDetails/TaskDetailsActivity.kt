package com.example.todo.ui.taskDetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.R
import com.example.todo.dao.TasksDao
import com.example.todo.databinding.ActivityTaskDetailsBinding
import com.example.todo.db.TasksDatabase
import com.example.todo.model.Task
import com.example.todo.utils.Constants
import com.example.todo.utils.getHourIn12
import com.example.todo.utils.getTimeAmPm
import com.example.todo.utils.parcelable
import com.example.todo.utils.showDatePickerDialog
import com.example.todo.utils.showTimePickerDialog
import java.util.Calendar

class TaskDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTaskDetailsBinding
    lateinit var dao: TasksDao
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        onSaveClick()
        onSelectDateClick()
        onSelectTimeClick()
        setupToolbar()

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        val task = intent.parcelable<Task>(Constants.TASK_KEY) as Task
        binding.content.title.setText(task.title)
        binding.content.description.setText(task.description)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = task.dateTime!!
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val minuteString = if (minutes == 0) "00" else minutes.toString()

        binding.content.selectDateTv.text = "$day/${month + 1}/$year"

        binding.content.selectTimeTv.text =
            "${getHourIn12(hour)}:${minuteString} ${getTimeAmPm(hour)}"

    }

    private fun onSelectTimeClick() {
        binding.content.selectTimeTil.setOnClickListener {
            val calendar = Calendar.getInstance()
            showTimePickerDialog(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                title = "Select task time",
                supportFragmentManager
            ) { hour, minutes ->
                val minuteString = if (minutes == 0) "00" else minutes.toString()
                binding.content.selectTimeTv.text =
                    "${getHourIn12(hour)}:${minuteString} ${getTimeAmPm(hour)}"
                this.calendar.set(Calendar.HOUR_OF_DAY, hour)
                this.calendar.set(Calendar.MINUTE, minutes)
                this.calendar.set(Calendar.SECOND, 0)
                this.calendar.set(Calendar.MILLISECOND, 0)
            }

        }
    }

    private fun onSelectDateClick() {
        binding.content.selectDateTil.setOnClickListener {
            showDatePickerDialog(this) { date, calendar ->
                this.calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                this.calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                this.calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                binding.content.selectDateTv.text = date
            }
        }

    }

    private fun onSaveClick() {
        binding.content.btnSave.setOnClickListener {
            updateTask()
        }
    }

    override fun onStart() {
        super.onStart()
        dao = TasksDatabase.getInstance(this).tasksDao()
    }

    private fun updateTask() {
        if (!isValid()) {
            return
        }
        val task = intent.parcelable<Task>(Constants.TASK_KEY) as Task
        task.title = binding.content.title.text.toString()
        task.description = binding.content.description.text.toString()
        task.dateTime = calendar.timeInMillis

        dao.updateTask(task)
        finish()


    }

    fun isValid(): Boolean {
        var isValid = true
        if (binding.content.title.text.isNullOrBlank()) {
            isValid = false
            binding.content.titleTil.error = getString(R.string.add_task_title)
        } else {
            binding.content.titleTil.error = null
        }

        if (binding.content.selectDateTv.text.isNullOrBlank()) {
            isValid = false
            binding.content.selectDateTil.error = getString(R.string.add_task_date)
        } else {
            binding.content.selectDateTil.error = null
        }

        if (binding.content.selectTimeTv.text.isNullOrBlank()) {
            isValid = false
            binding.content.selectTimeTil.error = getString(R.string.add_task_time)
        } else {
            binding.content.selectTimeTil.error = null
        }

        return isValid

    }

}