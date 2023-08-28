package com.example.todo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.R
import com.example.todo.dao.TasksDao
import com.example.todo.databinding.FragmentAddTaskBinding
import com.example.todo.db.TasksDatabase
import com.example.todo.model.Task
import com.example.todo.utils.getHourIn12
import com.example.todo.utils.getTimeAmPm
import com.example.todo.utils.showDatePickerDialog
import com.example.todo.utils.showTimePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class AddTaskFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAddTaskBinding
    lateinit var dao: TasksDao
    private var dateCalendar = Calendar.getInstance()
    private var timeCalendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSelectDateClick()
        onSelectTimeClick()
        onAddTaskClick()

    }

    private fun onSelectTimeClick() {
        binding.selectTimeTil.setOnClickListener {

            val calendar = Calendar.getInstance()

            showTimePickerDialog(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                title = "Select task time",
                childFragmentManager
            ) { hour, minutes ->
                val minuteString = if (minutes == 0) "00" else minutes.toString()
                binding.selectTimeTv.text =
                    "${getHourIn12(hour)}:${minuteString} ${getTimeAmPm(hour)}"
                this.timeCalendar.set(Calendar.YEAR, 0)
                this.timeCalendar.set(Calendar.MONTH, 0)
                this.timeCalendar.set(Calendar.DAY_OF_MONTH, 0)
                this.timeCalendar.set(Calendar.HOUR_OF_DAY, hour)
                this.timeCalendar.set(Calendar.MINUTE, minutes)
                this.timeCalendar.set(Calendar.SECOND, 0)
                this.timeCalendar.set(Calendar.MILLISECOND, 0)
            }

        }
    }

    private fun onAddTaskClick() {
        binding.addTaskBtn.setOnClickListener {
            createTask()
        }
    }

    private fun onSelectDateClick() {
        binding.selectDateTil.setOnClickListener {
            context?.let { context ->
                showDatePickerDialog(context) { date, calendar ->
                    this.dateCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                    this.dateCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                    this.dateCalendar.set(
                        Calendar.DAY_OF_MONTH,
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    this.dateCalendar.set(Calendar.HOUR_OF_DAY, 0)
                    this.dateCalendar.set(Calendar.MINUTE, 0)
                    this.dateCalendar.set(Calendar.SECOND, 0)
                    this.dateCalendar.set(Calendar.MILLISECOND, 0)
                    binding.selectDateTv.text = date
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dao = TasksDatabase.getInstance(requireContext()).tasksDao()

    }

    private fun createTask() {

        if (!isValid()) {
            return
        }

        val task = Task(
            title = binding.title.text.toString().trim(),
            description = binding.description.text.toString().trim(),
            date = dateCalendar.timeInMillis,
            time = timeCalendar.timeInMillis
        )

        dao.insertTask(task)
        onTaskAddedListener?.onTaskAdded(task)

        dismiss()


    }

    fun isValid(): Boolean {
        var isValid = true
        if (binding.title.text.isNullOrBlank()) {
            isValid = false
            binding.titleTil.error = getString(R.string.add_task_title)
        } else {
            binding.titleTil.error = null
        }

        if (binding.selectDateTv.text.isNullOrBlank()) {
            isValid = false
            binding.selectDateTil.error = getString(R.string.add_task_date)
        } else {
            binding.selectDateTil.error = null
        }

        if (binding.selectTimeTv.text.isNullOrBlank()) {
            isValid = false
            binding.selectTimeTil.error = getString(R.string.add_task_time)
        } else {
            binding.selectTimeTil.error = null
        }

        return isValid

    }

    var onTaskAddedListener: OnTaskAddedListener? = null

    fun interface OnTaskAddedListener {
        fun onTaskAdded(task: Task)
    }
}