package com.accsell.todoapp

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoItemsRepository {
    private val items = mutableListOf<TodoItem>()
    private val completedTaskItems = mutableListOf<TodoItem>()

    init {
        repeat(20) { index ->
            items.add(
                TodoItem(
                    text = "Задание #${index + 1}",
                    importance = when (index % 3) {
                        0 -> Importance.НИЗКАЯ
                        1 -> Importance.ОБЫЧНАЯ
                        else -> Importance.ВЫСОКАЯ
                    },
                    deadline = "${index + 1} декабря 2024"
                )
            )
        }
    }

    fun getAllItems(): List<TodoItem> {
        return items.toList()
    }

    fun getCompletedTaskItems(): List<TodoItem> {
        return completedTaskItems.toList()
    }

    fun addItem(item: TodoItem) {
        items.add(item)
    }

    fun deleteItem(item: TodoItem) {
        items.remove(item)
    }

    fun markItemAsCompleted(id: String) {
        items.find { it.id == id }?.let { item ->
            val updatedItem = item.copy(isCompleted = true, modifiedAt = Date().toString())
            items[items.indexOf(item)] = updatedItem
        }
    }

    fun updateItem(updatedItem: TodoItem) {
        val index = items.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            val existingItem = items[index]
            items[index] = updatedItem.copy(
                createdAt = existingItem.createdAt,
                modifiedAt = SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(Date())
            )
        }
    }


}
