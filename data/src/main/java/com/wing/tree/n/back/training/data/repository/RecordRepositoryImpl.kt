package com.wing.tree.n.back.training.data.repository

import com.wing.tree.n.back.training.data.datasource.local.record.RecordDataSource
import com.wing.tree.n.back.training.data.mapper.RecordMapper.toEntity
import com.wing.tree.n.back.training.domain.model.Record
import com.wing.tree.n.back.training.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(private val dataSource: RecordDataSource) : RecordRepository {
    override fun nList(): Flow<List<Int>> {
        return dataSource.nList()
    }

    override fun recordList(n: Int): Flow<List<Record>> {
        return dataSource.recordList(n)
    }

    override suspend fun insert(record: Record) {
        dataSource.insertRecord(record.toEntity())
    }
}