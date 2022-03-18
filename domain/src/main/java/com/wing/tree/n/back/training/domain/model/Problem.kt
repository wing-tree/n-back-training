package com.wing.tree.n.back.training.domain.model

import com.wing.tree.n.back.training.domain.util.notNull

abstract class Problem {
    abstract val number: Int
    abstract val solution: Boolean?
    abstract var answer: Boolean?

    val correct get() = answer.notNull && answer == solution
}