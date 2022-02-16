package com.wing.tree.n.back.training.domain.model

abstract class Record {
    abstract val back: Int
    abstract val problemList: List<Problem>
    abstract val rounds: Int
    abstract val speed: Int
    abstract val time: Long

    open val id: Long = 0L
}