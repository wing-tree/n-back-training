package com.wing.tree.n.back.training.data.mapper

import com.wing.tree.n.back.training.data.entity.Problem as Entity
import com.wing.tree.n.back.training.domain.model.Problem

internal object ProblemMapper {
    fun Problem.toEntity() =  Entity(
        solution = solution,
        number = number,
        answer = answer
    )
}