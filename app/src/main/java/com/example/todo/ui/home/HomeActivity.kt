package com.example.todo.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private var tasksFragment: TasksFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tasksFragment = TasksFragment()

        showFragment(tasksFragment!!)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.tasks) {
                showFragment(tasksFragment!!)
            } else if (menuItem.itemId == R.id.settings) {
                showFragment(SettingsFragment())
            }
            true
        }

        onAddTaskClicked()


    }

    private fun onAddTaskClicked() {
        binding.fabAddTask.setOnClickListener {
            showAddTaskBottomSheet()
        }
    }

    private fun showAddTaskBottomSheet() {
        val bottomSheet = AddTaskFragment()
        bottomSheet.onTaskAddedListener = AddTaskFragment.OnTaskAddedListener { task ->
            tasksFragment!!.loadAllTasksOfDate(task.date!!)

        }
        bottomSheet.show(supportFragmentManager, "")
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .commit()
    }
}