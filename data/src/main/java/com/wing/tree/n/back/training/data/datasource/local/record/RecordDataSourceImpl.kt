package com.wing.tree.n.back.training.data.datasource.local.record

import com.wing.tree.n.back.training.data.database.Database
import com.wing.tree.n.back.training.data.entity.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordDataSourceImpl @Inject constructor(database: Database) : RecordDataSource {
    private val dao = database.recordDao()

    override fun getRecordList(): Flow<List<Record>> {
        return dao.getRecordList()
    }

    override suspend fun insertRecord(record: Record) {
        dao.insert(record)
    }
}