package com.wing.tree.n.back.training.data.model

import com.google.firebase.Timestamp
import com.wing.tree.n.back.training.data.constant.BLANK

data class Ranking(
    val elapsedTime: Long = 0L,
    val n: Int = 2,
    val nation: String = BLANK,
    val nickname: String = BLANK,
    val rounds: Int = 30,
    val timestamp: Timestamp = Timestamp.now(),
    var id: String = BLANK,
) {
    fun isHigher(other: Ranking): Boolean {
        if (other.n > n) return true
        if (other.rounds > rounds) return true
        if (other.elapsedTime < other.elapsedTime) return true

        return false
    }
}