package com.accsell.todoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.accsell.todoapp.viewModel.TaskViewModel

@Composable
fun TodoApp(taskViewModel: TaskViewModel) {
    val navController = rememberNavController()

    // Main Navigation Host
    NavHost(navController, startDestination = "main") {
        // Main Screen displaying the list of tasks
        composable("main") {
            MainScreen(
                navController = navController,
                taskViewModel
            )
        }

        // Create Todo Screen for adding a new task
        composable("create_todo") {
            CreateTodoScreen(
                navController = navController,
                onDelete = { todoItem ->
                    navController.popBackStack()
                    taskViewModel.deleteTask(todoItem)
                },
                onSave = { todoItem ->
                    taskViewModel.addTask(todoItem)
                    navController.popBackStack()
                }
            )
        }

        // Edit Todo Screen, initialized with the specific task to edit
        composable(
            "edit_todo/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId") ?: ""
            val todoItem = taskViewModel.getTaskById(todoId)

            CreateTodoScreen(
                navController = navController,
                onDelete = { task ->
                    navController.popBackStack()
                    taskViewModel.deleteTask(task)
                },
                onSave = { updatedItem ->
                    taskViewModel.updateTask(updatedItem.copy(id = todoId))
                    navController.popBackStack()
                }
            )
        }
    }
}

