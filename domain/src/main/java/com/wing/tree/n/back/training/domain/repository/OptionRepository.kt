package com.wing.tree.n.back.training.domain.repository

import com.wing.tree.n.back.training.domain.model.Option

interface OptionRepository {
    suspend fun option(): Option
    suspend fun update(option: Option)
}