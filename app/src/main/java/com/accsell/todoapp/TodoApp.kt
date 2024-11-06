package com.accsell.todoapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun TodoApp(repository: TodoItemsRepository) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController, repository) }
        composable("create_todo") {
            CreateTodoScreen(
                navController,
                onDelete = { todoItem ->
                    navController.popBackStack()
                    repository.deleteItem(todoItem)
                },
                onSave = { todoItem ->
                    repository.addItem(todoItem)
                }
            )
        }
        composable(
            "edit_todo/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")
            val todoItem = repository.getAllItems().find { it.id == todoId }
            if (todoItem != null) {
                CreateTodoScreen(
                    navController = navController,
                    onDelete = { todoItem ->
                        navController.popBackStack()
                        repository.deleteItem(todoItem)
                    },
                    onSave = { updatedItem ->
                        repository.updateItem(updatedItem.copy(id = todoId.toString()))
                    },
                    initialTodoItem = todoItem
                )
            }
        }
    }
}