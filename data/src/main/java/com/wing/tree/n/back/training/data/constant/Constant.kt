package com.wing.tree.n.back.training.data.constant

internal const val BLANK = ""
internal const val PACKAGE_NAME = "com.wing.tree.n.back.training.data"

internal object Rounds {
    const val DEFAULT = 20
}

internal object Speed {
    const val DEFAULT = 3
}

internal val Int.long: Long
    get() = toLong()