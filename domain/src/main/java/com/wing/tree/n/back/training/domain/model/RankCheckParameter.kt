package com.wing.tree.n.back.training.domain.model

import com.wing.tree.n.back.training.domain.util.not

data class RankCheckParameter (
    val elapsedTime: Long,
    val n: Int,
    val rounds: Int
) {
    fun isHigher(other: RankCheckParameter): Boolean {
        if (other.n.not(n)) {
            return other.n > n
        }

        if (other.rounds.not(rounds)) {
            return other.rounds > rounds
        }

        return elapsedTime > other.elapsedTime
    }
}
