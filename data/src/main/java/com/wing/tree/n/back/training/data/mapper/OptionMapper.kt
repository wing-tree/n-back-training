package com.wing.tree.n.back.training.data.mapper

import com.wing.tree.n.back.training.data.datastore.option.Option
import com.wing.tree.n.back.training.domain.model.Option as Model

internal object OptionMapper {
    fun Option.toModel() = object : Model() {
        override var rounds = this@toModel.rounds
        override var speed = this@toModel.speed
    }
}