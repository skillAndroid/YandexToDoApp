package com.accsell.todoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.accsell.todoapp.ui.theme.ToDoAppTheme
import com.accsell.todoapp.ui.theme.primaryLightColor

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = TodoItemsRepository() // Создаем репозиторий

        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = primaryLightColor
                ) { padding ->
                    Column(modifier = Modifier.padding()) {
                        TodoApp(repository = repository)
                    }

                }
            }
        }
    }
}


