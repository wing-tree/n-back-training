package com.wing.tree.n.back.training.data.model

import com.google.firebase.Timestamp
import com.wing.tree.n.back.training.data.constant.BLANK
import com.wing.tree.n.back.training.domain.model.RankCheckParameter

data class Ranking(
    val country: String = BLANK,
    val elapsedTime: Long = 0L,
    val n: Int = 0,
    val name: String = BLANK,
    val rounds: Int = 0,
    val speed: Int = 0,
    val timestamp: Timestamp = Timestamp.now(),
    var id: String = BLANK,
) {
    private val rankCheckParameter: RankCheckParameter
        get() = RankCheckParameter(
            elapsedTime = elapsedTime,
            n = n,
            rounds = rounds
        )

    fun isHigher(other: RankCheckParameter): Boolean = rankCheckParameter.isHigher(other)
}