@file:Suppress("UNREACHABLE_CODE", "DEPRECATION")

package com.accsell.todoapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.accsell.todoapp.api.Task
import com.accsell.todoapp.ui.theme.my_blue_color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTodoScreen(
    navController: NavController,
    onSave: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    initialTodoItem: Task? = null
) {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

    var tempText by remember { mutableStateOf(TextFieldValue(initialTodoItem?.text ?: "")) }
    var tempImportance by remember {
        mutableStateOf(
            initialTodoItem?.importance ?: Importance.ОБЫЧНАЯ
        )
    }
    var tempDeadline by remember {
        mutableStateOf(initialTodoItem?.deadline?.let {
            try {
                dateFormat.parse(it.toString())
            } catch (e: Exception) {
                null
            }
        })
    }

    var text by remember { mutableStateOf(TextFieldValue(initialTodoItem?.text ?: "")) }
    var importance by remember { mutableStateOf(initialTodoItem?.importance ?: Importance.ОБЫЧНАЯ) }
    var deadline by remember { mutableStateOf<Date?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val currentDate = System.currentTimeMillis() // Get the current time in milliseconds
    val lastUpdatedBy = "user_id" // Set this to the ID of the user who is updating the task

    val todoItem = Task(
        id = initialTodoItem?.id ?: UUID.randomUUID().toString(),
        text = tempText.text,
        importance = tempImportance,
        deadline = tempDeadline?.time, // tempDeadline is assumed to be a Date object
        done = initialTodoItem?.done ?: false,
        color = null, // This might come from the UI or be null
        createdAt = initialTodoItem?.createdAt ?: currentDate, // Use currentDate if not available
        changedAt = if (initialTodoItem != null) currentDate else currentDate, // Update with the current timestamp
        lastUpdatedBy = lastUpdatedBy // Set the user ID or the identifier of the user making the change
    )

    LaunchedEffect(initialTodoItem) {
        initialTodoItem?.deadline?.let {
            try {
                tempDeadline = dateFormat.parse(it.toString())
            } catch (e: Exception) {
            }
        }
    }
    val isNoWork = tempText.text.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.offset(x = (-16).dp),
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(imageVector = Icons.Default.Clear, null, tint = Color.Black)
            }
            TextButton(
                modifier = Modifier.offset(x = 8.dp),
                enabled = tempText.text.isNotEmpty(),
                onClick = {
                    if (tempText.text.isNotEmpty()) {
                        text = tempText
                        importance = tempImportance
                        deadline = tempDeadline
                        onSave(todoItem)
                        navController.popBackStack()
                    }
                }
            ) {
                Text(
                    text = "Сохранить",
                    color = if (tempText.text.isNotEmpty()) my_blue_color else Color.LightGray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
        }

        androidx.compose.material.Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            elevation = 2.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(8.dp)
        ) {
            TextField(
                value = tempText,
                onValueChange = { tempText = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 12,
                placeholder = {
                    Text(text = "Что надо сделать…", color = Color.LightGray)
                },
                textStyle = TextStyle(fontSize = 16.sp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = true
                }
        ) {
            Text("Важность", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.Black)
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    when (tempImportance) {
                        Importance.НИЗКАЯ -> "НИЗКАЯ"
                        Importance.ОБЫЧНАЯ -> "ОБЫЧНАЯ"
                        Importance.ВЫСОКАЯ -> "!! ВЫСОКАЯ"
                    },
                    color = if (tempImportance == Importance.ВЫСОКАЯ) Color.Red else Color.Gray.copy(0.8f),
                    fontSize = 12.sp
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .background(Color.White)
                ) {
                    Importance.entries.forEach { importanceOption ->
                        DropdownMenuItem(
                            onClick = {
                                tempImportance = importanceOption
                                expanded = false
                            },
                            text = {
                                if (importanceOption== Importance.ВЫСОКАЯ){
                                    Text(
                                        "!! ВЫСОКАЯ",
                                        color = Color.Red,
                                    )
                                }else{
                                    Text(
                                        when (importanceOption) {
                                            Importance.НИЗКАЯ -> "НИЗКАЯ"
                                            Importance.ОБЫЧНАЯ -> "ОБЫЧНАЯ"
                                            Importance.ВЫСОКАЯ -> "ВЫСОКАЯ"
                                        },
                                    )
                                }

                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Divider(thickness = 1.dp, color = Color.LightGray)
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Сделать до", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.Black)
                androidx.compose.material.Switch(
                    checked = tempDeadline != null,
                    onCheckedChange = { checked ->
                        if (!checked) {
                            tempDeadline = null
                            showDatePicker = false
                        } else {
                            showDatePicker = true
                        }
                    },
                    colors = androidx.compose.material.SwitchDefaults.colors(
                        checkedThumbColor = my_blue_color,
                        uncheckedThumbColor = Color.White
                    )
                )
            }
            Text(tempDeadline?.let { dateFormat.format(it) } ?: "",
                modifier = Modifier.offset(y = (-8).dp),
                color = my_blue_color)
            Spacer(Modifier.height(12.dp))
            Divider(thickness = 1.dp, color = Color.LightGray)
        }
        Spacer(modifier = Modifier.height(8.dp))


        TextButton(
            onClick = {

                if (initialTodoItem != null) {
                    onDelete(initialTodoItem)
                } else {
                    navController.popBackStack()
                }


            },
            enabled = isNoWork || initialTodoItem != null,
            modifier = Modifier.offset(x = (-12).dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    null,
                    tint = if (isNoWork || initialTodoItem != null) Color.Red else Color.LightGray
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    "Удалить",
                    color = if (isNoWork || initialTodoItem != null) Color.Red else Color.LightGray,
                    fontSize = 18.sp
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { selectedDate ->
                tempDeadline = selectedDate
                showDatePicker = false
            },
            onDismiss = {
                if (tempDeadline == null) {
                    showDatePicker = false
                }
            }
        )
    }
}



