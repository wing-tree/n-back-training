package com.wing.tree.n.back.training.domain.repository

import com.wing.tree.n.back.training.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    fun backList(): Flow<List<Int>>
    fun recordList(n: Int): Flow<List<Record>>
    suspend fun insert(record: Record)
}