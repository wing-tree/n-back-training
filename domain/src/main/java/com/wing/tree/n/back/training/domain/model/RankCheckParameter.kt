package com.wing.tree.n.back.training.domain.model

data class RankCheckParameter (
    val elapsedTime: Long,
    val n: Int,
    val rounds: Int
) {
    fun isHigher(other: RankCheckParameter): Boolean {
        if (other.n > n) return true
        if (other.rounds > rounds) return true
        if (other.elapsedTime < other.elapsedTime) return true

        return false
    }
}
