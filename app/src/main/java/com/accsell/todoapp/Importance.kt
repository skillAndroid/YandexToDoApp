package com.accsell.todoapp

import java.util.Date
import java.util.UUID

enum class Importance {
    НИЗКАЯ,
    ОБЫЧНАЯ,
    ВЫСОКАЯ
}

data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val importance: Importance,
    val deadline: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: String = Date().toString(),
    val modifiedAt: String = Date().toString()
)