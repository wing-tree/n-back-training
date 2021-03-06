package com.wing.tree.n.back.training.data.datasource.local.record

import com.wing.tree.n.back.training.data.database.Database
import com.wing.tree.n.back.training.data.entity.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordDataSourceImpl @Inject constructor(database: Database) : RecordDataSource {
    private val dao = database.recordDao()

    override fun getRecords(): Flow<List<Record>> {
        return dao.getRecords()
    }

    override suspend fun delete(record: Record) {
        dao.delete(record)
    }

    override suspend fun insert(record: Record) {
        dao.insert(record)
    }
}