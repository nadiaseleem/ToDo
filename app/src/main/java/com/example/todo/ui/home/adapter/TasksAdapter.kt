package com.example.todo.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ItemTaskBinding
import com.example.todo.model.Task
import com.example.todo.utils.getHourIn12
import com.example.todo.utils.getTimeAmPm
import java.util.Calendar

class TasksAdapter(var tasks: MutableList<Task>? = null) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = tasks?.size ?: 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks?.get(position)!!
        holder.binding.title.text = task.title
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = task.dateTime!!

        val hr = calendar.get(Calendar.HOUR)
        val min = calendar.get(Calendar.MINUTE)
        val minutesString = if (min == 0) "00" else min.toString()
        holder.binding.time.text = "${getHourIn12(hr)}:$minutesString ${getTimeAmPm(hr)}"

    }

    fun updateTasks(tasks: MutableList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun updateTask(task: Task, position: Int) {
        this.tasks?.set(position, task)
        notifyItemInserted(position)
    }
}