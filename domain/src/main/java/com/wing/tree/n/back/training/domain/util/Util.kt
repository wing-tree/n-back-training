package com.wing.tree.n.back.training.domain.util

internal fun Any?.`is`(other: Any?) = this == other
internal fun Any?.not(other: Any?) = this.`is`(other).not()

internal val Any?.notNull: Boolean get() = this.not(null)