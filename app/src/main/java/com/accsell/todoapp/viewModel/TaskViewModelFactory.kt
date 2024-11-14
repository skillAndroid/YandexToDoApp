package com.accsell.todoapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.accsell.todoapp.api.ApiService


class TaskViewModelFactory(
    private val apiService: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}