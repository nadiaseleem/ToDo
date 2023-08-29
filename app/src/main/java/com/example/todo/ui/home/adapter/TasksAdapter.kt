package com.example.todo.ui.home.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.ItemTaskBinding
import com.example.todo.model.Task
import com.example.todo.utils.getHourIn12
import com.example.todo.utils.getTimeAmPm
import java.util.Calendar

class TasksAdapter(var color: Int? = null, var tasks: MutableList<Task>? = null) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemTaskBinding, val color: Int?) :
        RecyclerView.ViewHolder(binding.root) {

        fun changeTaskStatus(isDone: Boolean, color: Int? = null) {
            if (isDone) {
                binding.draggingBar.setImageResource(R.drawable.dragging_bar_done)
                binding.title.setTextColor(Color.GREEN)
                binding.btnTaskIsDone.setBackgroundResource(R.drawable.done)


            } else {
                color?.let {
                    binding.title.setTextColor(it)
                    binding.draggingBar.setImageResource(R.drawable.dragging_bar)
                }
                binding.btnTaskIsDone.setBackgroundResource(R.drawable.check_mark)

            }
        }
    }

    fun setColor(color: Int) {
        this.color = color
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, color)
    }

    override fun getItemCount(): Int = tasks?.size ?: 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks?.get(position)!!
        holder.binding.title.text = task.title
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = task.time!!
        holder.changeTaskStatus(task.isDone, color)

        val hr = calendar.get(Calendar.HOUR)
        val min = calendar.get(Calendar.MINUTE)
        val minutesString = if (min == 0) "00" else min.toString()
        holder.binding.time.text = "${getHourIn12(hr)}:$minutesString ${getTimeAmPm(hr)}"

        onButtonClickedListener?.let { onButtonClickedListener ->
            holder.binding.btnTaskIsDone.setOnClickListener {
                onButtonClickedListener.onButtonClicked(position, task)
            }
        }
        onDeleteClickedListener?.let {
            holder.binding.leftView.setOnClickListener {
                onDeleteClickedListener?.onButtonClicked(position, task)
            }
        }

        onItemClickedListener?.let {
            holder.binding.dragItem.setOnClickListener {
                onItemClickedListener?.onButtonClicked(position, task)
            }
        }

    }

    fun updateTasks(tasks: MutableList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun updateTask(task: Task, position: Int) {
        this.tasks?.set(position, task)
        notifyItemChanged(position)
    }

    fun deleteTask(task: Task) {
        this.tasks?.remove(task)
        notifyDataSetChanged()
    }

    var onItemClickedListener: OnItemClickedListener? = null
    var onButtonClickedListener: OnItemClickedListener? = null
    var onDeleteClickedListener: OnItemClickedListener? = null

    fun interface OnItemClickedListener {
        fun onButtonClicked(position: Int, task: Task)
    }

}