package com.wing.tree.n.back.training.domain.model

abstract class Problem {
    abstract val number: Int
    abstract val solution: Boolean?
    abstract var answer: Boolean?

    val isCorrect get() = answer != null && answer == solution
}