package com.wing.tree.n.back.training.data.datasource.local.record

import com.wing.tree.n.back.training.data.database.Database
import com.wing.tree.n.back.training.data.entity.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordDataSourceImpl @Inject constructor(database: Database) : RecordDataSource {
    private val dao = database.recordDao()

    override fun getBackList(): Flow<List<Int>> {
        return dao.backList()
    }

    override fun getRecordList(n: Int): Flow<List<Record>> {
        return dao.recordList(n)
    }

    override suspend fun insertRecord(record: Record) {
        dao.insert(record)
    }
}