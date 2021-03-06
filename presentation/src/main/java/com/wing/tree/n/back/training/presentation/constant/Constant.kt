package com.wing.tree.n.back.training.presentation.constant

internal const val BLANK = ""
internal const val ONE_HUNDRED = 100
internal const val ONE_SECOND = 1000L
internal const val PACKAGE_NAME = "com.wing.tree.n.back.training.presentation"

internal object N {
    const val DEFAULT = 2

    val IntRange = IntRange(2, 9)
}

internal object Rounds {
    const val DEFAULT = 20
    const val STEPS = 3

    val ValueRange = 10.0F..50.0F
}

internal object Speed {
    const val DEFAULT = 3
    const val STEPS = 3

    val ValueRange = 1F..5F
}

internal object SpeedMode {
    const val DEFAULT = false
}

internal object Random {
    const val FROM = 0
    const val OFFSET = 4
    const val UNTIL = 4
}