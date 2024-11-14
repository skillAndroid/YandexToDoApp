package com.accsell.todoapp.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.accsell.todoapp.Importance
import com.google.gson.annotations.SerializedName

data class TaskListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("list") val taskList: List<Task>,
    @SerializedName("revision") val revision: Int
)

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Long?,
    val done: Boolean,
    val color: String?,
    val createdAt: Long,
    val changedAt: Long,
    val lastUpdatedBy: String
)