package com.accsell.todoapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("list")
    suspend fun getTasks(): Response<TaskListResponse>

    @PATCH("list")
    suspend fun updateTasks(@Body tasks: List<Task>): Response<TaskListResponse>

    @POST("list")
    suspend fun addTask(@Body task: Task): Response<Task>

    @DELETE("list/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Task>

    @GET("list/{id}")
    suspend fun getTask(@Path("id") id: String): Response<Task>
}
