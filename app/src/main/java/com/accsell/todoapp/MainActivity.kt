package com.accsell.todoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.accsell.todoapp.api.ApiClient
import com.accsell.todoapp.api.ApiService
import com.accsell.todoapp.ui.theme.ToDoAppTheme
import com.accsell.todoapp.ui.theme.primaryLightColor
import com.accsell.todoapp.viewModel.TaskViewModel
import com.accsell.todoapp.viewModel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var apiService: ApiService
    private lateinit var taskViewModel: TaskViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure the token is passed correctly. If your token is nullable, handle it gracefully.
        val token = "your_token_here" // Replace with actual token or retrieve it securely.

        if (token.isNotEmpty()) {
            val apiClient = ApiClient(token)
            apiService = apiClient.retrofit.create(ApiService::class.java)

            val taskViewModelFactory = TaskViewModelFactory(apiService)
            taskViewModel = ViewModelProvider(this, taskViewModelFactory).get(TaskViewModel::class.java)

            enableEdgeToEdge()
            setContent {
                ToDoAppTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = primaryLightColor
                    ) { padding ->
                        Column(modifier = Modifier.padding()) {
                            TodoApp(taskViewModel)
                        }
                    }
                }
            }
        } else {
            // Handle the case where the token is not available
            // e.g., show a message or fallback to some default state
            Log.e("MainActivity", "API Token is missing or invalid.")
        }
    }
}


