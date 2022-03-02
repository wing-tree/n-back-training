package com.wing.tree.n.back.training.data.repository

import com.wing.tree.n.back.training.data.datasource.local.record.RecordDataSource
import com.wing.tree.n.back.training.data.mapper.RecordMapper.toEntity
import com.wing.tree.n.back.training.domain.model.Record
import com.wing.tree.n.back.training.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(private val dataSource: RecordDataSource) : RecordRepository {
    override fun getRecords(): Flow<List<Record>> {
        return dataSource.getRecords()
    }

    override suspend fun insert(record: Record) {
        dataSource.insertRecord(record.toEntity())
    }
}