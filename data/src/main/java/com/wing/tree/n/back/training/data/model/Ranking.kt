package com.wing.tree.n.back.training.data.model

import com.google.firebase.Timestamp

data class Ranking(
    val elapsedTime: Long = 0L,
    val n: Int = 2,
    val nation: String = "",
    val nickname: String = "",
    val rounds: Int = 30,
    val timestamp: Timestamp = Timestamp.now()
) {
    fun isHigher(other: Ranking): Boolean {
        if (other.n > n) return true
        if (other.rounds > rounds) return true
        if (other.elapsedTime < other.elapsedTime) return true

        return false
    }
}