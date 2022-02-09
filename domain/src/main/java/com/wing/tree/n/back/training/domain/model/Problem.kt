package com.wing.tree.n.back.training.domain.model

abstract class Problem {
    abstract val solution: Boolean?
    abstract val value: Int
    abstract var answer: Boolean?

    val isCorrect get() = answer != null && answer == solution
}