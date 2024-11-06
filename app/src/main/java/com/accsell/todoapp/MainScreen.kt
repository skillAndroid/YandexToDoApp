package com.accsell.todoapp

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.accsell.todoapp.ui.theme.my_blue_color
import com.accsell.todoapp.ui.theme.my_primary_color
import com.accsell.todoapp.ui.theme.my_secondary_color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

@SuppressLint("CoroutineCreationDuringComposition", "UseOfNonLambdaOffsetOverload")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)

@Composable
fun MainScreen(navController: NavController, repository: TodoItemsRepository) {
    val todoItems = remember { mutableStateListOf(*repository.getAllItems().toTypedArray()) }
    val completedItems =
        remember { mutableStateListOf(*repository.getCompletedTaskItems().toTypedArray()) }
    var showCompleted by rememberSaveable { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        snapAnimationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val collapsedFraction by remember { derivedStateOf { scrollBehavior.state.collapsedFraction } }




    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = lerp(16.dp, 24.dp, collapsedFraction),
                                bottom = lerp(8.dp, 8.dp, collapsedFraction),
                                start = lerp(44.dp, 0.dp, collapsedFraction),
                                end = lerp(32.dp, 16.dp, collapsedFraction)
                            )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Мои дела",
                                fontSize = lerp(40.sp, 26.sp, collapsedFraction),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.graphicsLayer {
                                    translationY = -15f * collapsedFraction
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        alpha = collapsedFraction
                                        scaleX = collapsedFraction
                                        scaleY = collapsedFraction
                                    }
                            ) {
                                IconButton(
                                    onClick = {
                                        if (!showCompleted) {
                                            scope.launch {
                                                showCompleted = true
                                                listState.animateScrollToItem(0)
                                            }
                                        } else {
                                            showCompleted = false
                                        }
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (showCompleted)
                                                R.drawable.visibility_off
                                            else R.drawable.visibility
                                        ),
                                        contentDescription = if (showCompleted)
                                            "Hide completed"
                                        else "Show completed",
                                        modifier = Modifier.size(24.dp),
                                        tint = my_blue_color
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    alpha = 1f - collapsedFraction
                                    translationY = 30f * collapsedFraction
                                }
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = 0.8f,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Выполнено — ${completedItems.size}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                IconButton(
                                    onClick = {
                                        if (!showCompleted) {
                                            scope.launch {
                                                showCompleted = true
                                                listState.animateScrollToItem(0)
                                            }
                                        } else {
                                            showCompleted = false
                                        }
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (showCompleted)
                                                R.drawable.visibility_off
                                            else R.drawable.visibility
                                        ),
                                        contentDescription = if (showCompleted)
                                            "Hide completed"
                                        else "Show completed",
                                        modifier = Modifier.size(24.dp),
                                        tint = my_blue_color
                                    )
                                }
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .height(lerp(180.dp, 90.dp, collapsedFraction))
                    .shadow(
                        elevation = lerp(0.dp, 8.dp, collapsedFraction),
                        spotColor = Color.Black
                    )
            )
        }
    ) { paddingValues ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, start = 8.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(
                    topStart = lerp(8.dp, 0.dp, collapsedFraction),
                    topEnd = lerp(8.dp, 0.dp, collapsedFraction),
                    bottomStart = lerp(8.dp, 0.dp, collapsedFraction),
                    bottomEnd = lerp(8.dp, 0.dp, collapsedFraction),
                ),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                LazyColumn(state = listState) {
                    if (showCompleted) {
                        items(completedItems) { item ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(8.dp)
                            ) {
                                Checkbox(
                                    checked = true,
                                    onCheckedChange = {},
                                    colors = androidx.compose.material3.CheckboxDefaults.colors(
                                        checkedColor = my_primary_color,
                                        uncheckedColor = my_secondary_color,
                                        disabledUncheckedColor = my_secondary_color,
                                    )
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(top = 13.dp)
                                        .weight(1f)
                                ) {
                                    Column {
                                        Row {
                                            Text(
                                                text = item.text,
                                                color = Color.Gray,
                                                maxLines = 3,
                                                fontSize = 18.sp,
                                                overflow = TextOverflow.Ellipsis,
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                        }

                                        Row {
                                            Text(
                                                text = "Выполнено",
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                }
                                IconButton(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .size(28.dp),
                                    onClick = {}
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.info_outline),
                                        null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    items(todoItems, key = { it.id }) { item ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = { dismissValue ->
                                when (dismissValue) {
                                    DismissValue.DismissedToStart -> {
                                        scope.launch {
                                            delay(40)
                                            repository.deleteItem(item)
                                            todoItems.remove(item)

                                        }
                                        true
                                    }

                                    DismissValue.DismissedToEnd -> {
                                        scope.launch {
                                            delay(40)
                                            repository.markItemAsCompleted(item.id)
                                            completedItems.add(item)
                                            todoItems.remove(item)
                                        }
                                        true
                                    }

                                    else -> false
                                }
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(
                                DismissDirection.EndToStart,
                                DismissDirection.StartToEnd
                            ),
                            dismissThresholds = { FractionalThreshold(0.6f) },
                            background = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            if (dismissState.dismissDirection == DismissDirection.EndToStart)
                                                Color.Red else Color.Green
                                        )
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = if (dismissState.dismissDirection == DismissDirection.EndToStart)
                                        Alignment.CenterEnd else Alignment.CenterStart
                                ) {
                                    Icon(
                                        imageVector = if (dismissState.dismissDirection == DismissDirection.EndToStart)
                                            Icons.Default.Delete else Icons.Default.Done,
                                        contentDescription = if (dismissState.dismissDirection == DismissDirection.EndToStart)
                                            "Delete" else "Complete",
                                        tint = Color.White
                                    )
                                }
                            },
                            dismissContent = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .animateItemPlacement(),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    val offsetX by animateDpAsState(
                                        targetValue = if (dismissState.targetValue == DismissValue.DismissedToStart) (-1000).dp else if (dismissState.targetValue == DismissValue.DismissedToEnd) 1000.dp else 0.dp,
                                        label = ""
                                    )
                                    val isChecked = remember { mutableStateOf(item.isCompleted) }
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .offset(x = offsetX)
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    ) {
                                        Checkbox(
                                            checked = isChecked.value,
                                            onCheckedChange = { checked ->
                                                isChecked.value = checked
                                                if (checked) {
                                                    scope.launch {
                                                        repository.markItemAsCompleted(item.id)
                                                        delay(400)
                                                        completedItems.add(item.copy(isCompleted = true))
                                                        todoItems.remove(item)
                                                    }

                                                } else {
                                                    val updatedItem =
                                                        item.copy(
                                                            isCompleted = false,
                                                            modifiedAt = Date().toString()
                                                        )
                                                    repository.addItem(updatedItem)
                                                    todoItems[todoItems.indexOf(item)] = updatedItem
                                                }
                                            },
                                            colors = androidx.compose.material3.CheckboxDefaults.colors(
                                                checkedColor = my_primary_color,
                                                uncheckedColor = if (item.importance == Importance.ВЫСОКАЯ) Color.Red else my_secondary_color,
                                                disabledUncheckedColor = if (item.importance == Importance.ВЫСОКАЯ) Color.Red else my_secondary_color,
                                            )
                                        )
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 13.dp)
                                                .weight(1f)
                                        ) {
                                            Column {
                                                Row {
                                                    if (
                                                        !isChecked.value && item.importance == Importance.ВЫСОКАЯ
                                                    ) {
                                                        Text(
                                                            "!! ",
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = Color.Red,
                                                            fontSize = 24.sp
                                                        )
                                                    }
                                                    Text(
                                                        text = item.text,
                                                        color = if (isChecked.value) Color.Gray else Color.Black,
                                                        maxLines = 3,
                                                        fontSize = 18.sp,
                                                        overflow = TextOverflow.Ellipsis,
                                                        textDecoration = if (isChecked.value) TextDecoration.LineThrough else TextDecoration.None
                                                    )
                                                }

                                                Row {
                                                    Text(
                                                        text = item.deadline ?: "Нет дедлайна",
                                                        color = Color.Gray,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        textDecoration = if (isChecked.value) TextDecoration.LineThrough else TextDecoration.None
                                                    )
                                                }
                                            }

                                        }
                                        IconButton(
                                            modifier = Modifier
                                                .padding(top = 8.dp)
                                                .size(28.dp),
                                            onClick = {
                                                navController.navigate("edit_todo/${item.id}")
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.info_outline),
                                                null,
                                                tint = Color.Gray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }


                                }


                            }
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    navController.navigate("create_todo")
                },

                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                shape = CircleShape,
                containerColor = my_blue_color
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        }

    }
}