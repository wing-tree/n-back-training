package com.wing.tree.n.back.training.domain.model

import java.util.*

data class Ranking(
    val elapsedTime: Long,
    val n: Int,
    val nation: String,
    val nickname: String,
    val rounds: Int,
    val timestamp: Date
)