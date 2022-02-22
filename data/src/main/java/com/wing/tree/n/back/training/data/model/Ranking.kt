package com.wing.tree.n.back.training.data.model

import com.google.firebase.Timestamp

data class Ranking(
    val elapsedTime: Long,
    val n: Int,
    val nation: String,
    val nickname: String,
    val rounds: Int,
    val timestamp: Timestamp = Timestamp.now()
) {
    fun isHigher(other: Ranking): Boolean {
        if (other.n > n) return true
        if (other.rounds > rounds) return true
        if (other.elapsedTime < other.elapsedTime) return true

        return false
    }
}