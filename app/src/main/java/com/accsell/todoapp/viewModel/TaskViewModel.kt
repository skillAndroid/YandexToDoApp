package com.accsell.todoapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accsell.todoapp.api.ApiService
import com.accsell.todoapp.api.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

class TaskViewModel(private val apiService: ApiService) : ViewModel() {

    private val _taskListState = MutableStateFlow<List<Task>>(emptyList())
    val taskListState: StateFlow<List<Task>> = _taskListState

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            try {
                Log.d("TaskViewModel", "Loading tasks...")
                val response = apiService.getTasks()
                if (response.isSuccessful) {
                    Log.d("TaskViewModel", "Tasks loaded successfully")
                    _taskListState.value = response.body()?.taskList ?: emptyList()
                } else {
                    Log.e("TaskViewModel", "Error loading tasks: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Exception while loading tasks", e)
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                Log.d("TaskViewModel", "Adding task: ${task.id}")
                val response = apiService.addTask(task)
                if (response.isSuccessful) {
                    Log.d("TaskViewModel", "Task added successfully")
                    loadTasks()
                } else {
                    Log.e("TaskViewModel", "Error adding task: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Exception while adding task", e)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                Log.d("TaskViewModel", "Updating task: ${task.id}")
                val response = apiService.updateTasks(listOf(task))
                if (response.isSuccessful) {
                    Log.d("TaskViewModel", "Task updated successfully")
                    loadTasks()
                } else {
                    Log.e("TaskViewModel", "Error updating task: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Exception while updating task", e)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                Log.d("TaskViewModel", "Deleting task: ${task.id}")
                val response = apiService.deleteTask(task.id)
                if (response.isSuccessful) {
                    Log.d("TaskViewModel", "Task deleted successfully")
                    loadTasks()
                } else {
                    Log.e("TaskViewModel", "Error deleting task: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Exception while deleting task", e)
            }
        }
    }

    fun getTaskById(taskId: String) {
        viewModelScope.launch {
            try {
                Log.d("TaskViewModel", "Getting task by id: $taskId")
                val response = apiService.getTask(taskId)
                if (response.isSuccessful) {
                    Log.d("TaskViewModel", "Task retrieved successfully")
                    _task.value = response.body()
                } else {
                    Log.e("TaskViewModel", "Error retrieving task: ${response.code()}")
                    _task.value = null
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Exception while getting task", e)
                _task.value = null
            }
        }
    }

    fun getCompletedTasks(): List<Task> {
        Log.d("TaskViewModel", "Getting completed tasks")
        return taskListState.value.filter { it.done } ?: emptyList()
    }
}


