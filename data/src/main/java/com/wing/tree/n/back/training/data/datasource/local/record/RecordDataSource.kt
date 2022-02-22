package com.wing.tree.n.back.training.data.datasource.local.record

import com.wing.tree.n.back.training.data.entity.Record
import kotlinx.coroutines.flow.Flow

interface RecordDataSource {
    fun getRecordList(): Flow<List<Record>>
    suspend fun insertRecord(record: Record)
}