package com.wing.tree.n.back.training.data.mapper

import com.wing.tree.n.back.training.data.mapper.ProblemMapper.toEntity
import com.wing.tree.n.back.training.domain.model.Problem
import com.wing.tree.n.back.training.data.entity.Record as Entity
import com.wing.tree.n.back.training.domain.model.Record as Model

internal object RecordMapper {
    fun Model.toEntity(id: Long? = null): Entity = id?.let {
        Entity(
            id = it,
            elapsedTime = elapsedTime,
            n = n,
            problems = problems.map { it.toEntity() },
            rounds = rounds,
            speed = speed,
            timestamp = timestamp
        )
    } ?: Entity(
        elapsedTime = elapsedTime,
        n = n,
        problems = problems.map { it.toEntity() },
        rounds = rounds,
        speed = speed,
        timestamp = timestamp
    )
}