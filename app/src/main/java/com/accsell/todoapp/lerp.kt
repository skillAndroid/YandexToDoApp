package com.accsell.todoapp

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

fun lerp(start: TextUnit, stop: TextUnit, fraction: Float): TextUnit {
    return TextUnit(
        start.value + (stop.value - start.value) * fraction.coerceIn(0f, 1f),
        start.type
    )
}

fun lerp(start: Dp, stop: Dp, fraction: Float): Dp {
    return Dp(
        start.value + (stop.value - start.value) * fraction.coerceIn(0f, 1f)
    )
}