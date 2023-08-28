package com.example.todo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.dao.TasksDao
import com.example.todo.databinding.FragmentTasksBinding
import com.example.todo.db.TasksDatabase
import com.example.todo.model.Task
import com.example.todo.ui.home.adapter.TasksAdapter

class TasksFragment : Fragment() {
    lateinit var binding: FragmentTasksBinding
    lateinit var dao: TasksDao
    private var tasks = mutableListOf<Task>()
    private var tasksAdapter = TasksAdapter()
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

    }

    private fun initRecyclerView() {
        binding.rvTasks.adapter = tasksAdapter
    }

    override fun onStart() {
        super.onStart()
        dao = TasksDatabase.getInstance(requireContext()).tasksDao()
        loadAllTasks()
    }

    private fun loadAllTasks() {
        tasks = dao.getAllTasks().toMutableList()
        tasksAdapter.updateTasks(tasks)


    }
}