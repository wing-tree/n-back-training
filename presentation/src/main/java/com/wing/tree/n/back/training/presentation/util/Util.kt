@file:Suppress("unused")

package com.wing.tree.n.back.training.presentation.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

internal val Float.int get() = toInt()
internal val Float.roundedInt get() = roundToInt()

internal val Int.float get() = toFloat()
internal val Int.quarter get() = this / 4

internal val Long.int get() = this.toInt()
internal val Long.half get() = this / 2L
internal val Long.quarter get() = this / 4L
internal val Long.twice get() = this * 2L

internal val Long.seconds get() = TimeUnit.MILLISECONDS.toSeconds(this)

internal fun <T> nullOrThen(condition: Boolean, then: T): T? {
    return if (condition) {
        then
    } else {
        null
    }
}

@Composable
internal fun Dp.toSp() = with(LocalDensity.current) { this@toSp.toSp() }

val Locale.flagEmoji: String
    get() {
        val first = Character.codePointAt(country, 0) - 0x41 + 0x1F1E6
        val second = Character.codePointAt(country, 1) - 0x41 + 0x1F1E6

        return String(Character.toChars(first)) + String(Character.toChars(second))
    }

val String.flagEmoji: String
    get() {
        val first = Character.codePointAt(this, 0) - 0x41 + 0x1F1E6
        val second = Character.codePointAt(this, 1) - 0x41 + 0x1F1E6

        return String(Character.toChars(first)) + String(Character.toChars(second))
    }

inline fun <reified T: Activity> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}

fun <T> Boolean.ifElse(`if`: T, `else`: T) =
    if (this) {
        `if`
    } else {
        `else`
    }