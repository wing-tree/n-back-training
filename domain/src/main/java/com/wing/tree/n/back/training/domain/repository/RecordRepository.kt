package com.wing.tree.n.back.training.domain.repository

import com.wing.tree.n.back.training.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    fun getRecordList(): Flow<List<Record>>
    suspend fun insert(record: Record)
}