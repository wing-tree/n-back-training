package com.wing.tree.n.back.training.domain.model

abstract class Record {
    abstract val n: Int
    abstract val problemList: List<Problem>
    abstract val rounds: Int
    abstract val speed: Int
    abstract val timestamp: Long

    open val id: Long = 0L
}