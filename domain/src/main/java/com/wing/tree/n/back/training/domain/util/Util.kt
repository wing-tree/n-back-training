package com.wing.tree.n.back.training.domain.util

fun Any?.`is`(other: Any?) = this == other
fun Any?.not(other: Any?) = this.`is`(other).not()

val Any?.isNull get() = this == null
val Any?.notNull: Boolean get() = this.not(null)