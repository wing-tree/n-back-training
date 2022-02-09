package com.wing.tree.n.back.training.domain.repository

import com.wing.tree.n.back.training.domain.model.Option
import kotlinx.coroutines.flow.Flow

interface OptionRepository {
    fun option(): Flow<Option>
    suspend fun update(option: Option)
}