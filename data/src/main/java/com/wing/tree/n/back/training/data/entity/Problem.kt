package com.wing.tree.n.back.training.data.entity

import com.wing.tree.n.back.training.domain.model.Problem

data class Problem(
    override val solution: Boolean?,
    override val number: Int,
    override var answer: Boolean?
) : Problem()