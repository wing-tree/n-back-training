package com.wing.tree.n.back.training.domain.model

import com.wing.tree.n.back.training.domain.constant.BLANK
import java.util.*

data class Ranking(
    val country: String,
    val elapsedTime: Long,
    val n: Int,
    val name: String,
    val rounds: Int,
    val speed: Int,
    val timestamp: Date,
    var id: String = BLANK,
)