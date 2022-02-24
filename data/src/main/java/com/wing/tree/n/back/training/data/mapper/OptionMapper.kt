package com.wing.tree.n.back.training.data.mapper

import com.wing.tree.n.back.training.data.datastore.option.Option
import com.wing.tree.n.back.training.domain.model.Option as DomainModel

internal object OptionMapper {
    fun Option.toDomainModel() = object : DomainModel() {
        override var rounds = this@toDomainModel.rounds
        override var speed = this@toDomainModel.speed
    }
}