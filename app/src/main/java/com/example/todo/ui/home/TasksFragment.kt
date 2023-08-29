package com.example.todo.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.dao.TasksDao
import com.example.todo.databinding.FragmentTasksBinding
import com.example.todo.db.TasksDatabase
import com.example.todo.model.Task
import com.example.todo.ui.home.adapter.TasksAdapter
import com.example.todo.ui.taskDetails.TaskDetailsActivity
import com.example.todo.utils.Constants
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.Calendar

class TasksFragment : Fragment() {
    lateinit var binding: FragmentTasksBinding
    lateinit var dao: TasksDao
    private var tasks = mutableListOf<Task>()
    private var tasksAdapter = TasksAdapter()
    private var selectedDate: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initCalenderView()
        initSelectedDate()

    }

    private fun initSelectedDate() {
        val calendar = Calendar.getInstance()
        val today = CalendarDay.today()
        calendar.set(Calendar.YEAR, today.year)
        calendar.set(Calendar.MONTH, today.month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, today.day)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        selectedDate = calendar.timeInMillis
    }

    private fun initCalenderView() {
        binding.calendarView.selectedDate = CalendarDay.today()
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, date.year)
            calendar.set(Calendar.MONTH, date.month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, date.day)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            if (selected) {
                val tasks = dao.getTasksByDate(calendar.timeInMillis).toMutableList()
                tasksAdapter.updateTasks(tasks)
            }
        }

    }

    private fun initRecyclerView() {
        binding.rvTasks.adapter = tasksAdapter
        tasksAdapter.onButtonClickedListener =
            TasksAdapter.OnItemClickedListener { position, task ->
                task.isDone = !task.isDone
                dao.updateTask(task)
                tasksAdapter.updateTask(task, position)
            }

        tasksAdapter.onDeleteClickedListener =
            TasksAdapter.OnItemClickedListener { _, task ->
                dao.deleteTask(task)
                tasksAdapter.deleteTask(task)
            }
        tasksAdapter.onItemClickedListener =
            TasksAdapter.OnItemClickedListener { _, task ->
                val intent = Intent(activity, TaskDetailsActivity::class.java)
                intent.putExtra(Constants.TASK_KEY, task)
                startActivity(intent)
            }
    }

    override fun onStart() {
        super.onStart()
        activity?.let { dao = TasksDatabase.getInstance(it.applicationContext).tasksDao() }
        loadAllTasksOfDate(selectedDate)
        tasksAdapter.setColor(ContextCompat.getColor(requireContext(), R.color.blue))

    }


    fun loadAllTasksOfDate(date: Long) {
        tasks = dao.getTasksByDate(date).toMutableList()
        tasksAdapter.updateTasks(tasks)


    }
}