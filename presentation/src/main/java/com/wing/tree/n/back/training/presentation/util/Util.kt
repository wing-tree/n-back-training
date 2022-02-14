package com.wing.tree.n.back.training.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

internal fun Any?.`is`(other: Any?) = this == other
internal fun Any?.not(other: Any?) = this.`is`(other).not()

internal val Any?.isNull get() = this == null
internal val Any?.notNull get() = this.isNull.not()

internal val Float.int get() = toInt()
internal val Float.roundedInt get() = roundToInt()

internal val Int.float get() = toFloat()
internal val Int.quarter get() = this / 4

internal val Long.half get() = this / 2L
internal val Long.quarter get() = this / 4L
internal val Long.twice get() = this * 2L

internal fun <T> nullOrThen(condition: Boolean, then: T): T? {
    return if (condition) {
        then
    } else {
        null
    }
}

@Composable
internal fun Dp.toSp() = with(LocalDensity.current) { this@toSp.toSp() }