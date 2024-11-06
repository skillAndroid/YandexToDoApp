package com.accsell.todoapp

import java.text.SimpleDateFormat
import java.util.Locale


val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
val outputDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

fun formatDate(dateString: String): String {
    return try {
        val date = inputDateFormat.parse(dateString)
        outputDateFormat.format(date)
    } catch (e: Exception) {
        dateString
    }
}