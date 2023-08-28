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
import com.example.todo.utils.showDatePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class AddTaskFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAddTaskBinding
    lateinit var dao: TasksDao
    lateinit var calendar: Calendar

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
        onAddtaskClick()

    }

    private fun onAddtaskClick() {
        binding.addTaskBtn.setOnClickListener {
            createTask()
        }
    }

    private fun onSelectDateClick() {
        binding.selectTimeTil.setOnClickListener {
            context?.let { context ->
                showDatePickerDialog(context) { date, calendar ->
                    this.calendar = calendar
                    binding.selectTimeTv.text = date
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
            dateTime = calendar.timeInMillis
        )
        dao.insertTask(task)
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

        if (binding.selectTimeTv.text.isNullOrBlank()) {
            isValid = false
            binding.selectTimeTil.error = getString(R.string.add_task_date)
        } else {
            binding.selectTimeTil.error = null
        }

        return isValid

    }
}