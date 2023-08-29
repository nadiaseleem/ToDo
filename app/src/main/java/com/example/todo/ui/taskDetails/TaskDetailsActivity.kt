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
    private var dateCalendar = Calendar.getInstance()
    private var timeCalendar = Calendar.getInstance()
    private var myTask = Task()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        onSaveClick()
        onSelectDateClick()
        onSelectTimeClick()
        setupToolbar()
        val task = intent.parcelable<Task>(Constants.TASK_KEY) as Task
        myTask.date = task.date
        myTask.time = task.time

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        val task = intent.parcelable<Task>(Constants.TASK_KEY) as Task
        binding.content.title.setText(task.title)
        binding.content.description.setText(task.description)
        val dateCalendar = Calendar.getInstance()
        dateCalendar.timeInMillis = task.date!!
        val year = dateCalendar.get(Calendar.YEAR)
        val month = dateCalendar.get(Calendar.MONTH)
        val day = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val timeCalendar = Calendar.getInstance()
        timeCalendar.timeInMillis = task.time!!
        val hour = timeCalendar.get(Calendar.HOUR_OF_DAY)
        val minutes = timeCalendar.get(Calendar.MINUTE)
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
                this.timeCalendar.set(Calendar.YEAR, 0)
                this.timeCalendar.set(Calendar.MONTH, 0)
                this.timeCalendar.set(Calendar.DAY_OF_MONTH, 0)
                this.timeCalendar.set(Calendar.HOUR_OF_DAY, hour)
                this.timeCalendar.set(Calendar.MINUTE, minutes)
                this.timeCalendar.set(Calendar.SECOND, 0)
                this.timeCalendar.set(Calendar.MILLISECOND, 0)
                myTask.time = timeCalendar.timeInMillis
            }

        }
    }

    private fun onSelectDateClick() {
        binding.content.selectDateTil.setOnClickListener {
            showDatePickerDialog(this) { date, calendar ->
                this.dateCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                this.dateCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                this.dateCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                this.timeCalendar.set(Calendar.HOUR_OF_DAY, 0)
                this.timeCalendar.set(Calendar.MINUTE, 0)
                this.timeCalendar.set(Calendar.SECOND, 0)
                this.timeCalendar.set(Calendar.MILLISECOND, 0)
                binding.content.selectDateTv.text = date
                myTask.date = dateCalendar.timeInMillis

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
        task.date = dateCalendar.timeInMillis
        task.time = timeCalendar.timeInMillis

        myTask = task.copy(
            id = task.id,
            title = task.title,
            description = task.description,
            date = myTask.date,
            time = myTask.time,
            isDone = task.isDone
        )
        dao.updateTask(myTask)
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