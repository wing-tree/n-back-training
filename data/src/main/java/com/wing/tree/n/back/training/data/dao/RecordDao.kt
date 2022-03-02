package com.wing.tree.n.back.training.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.wing.tree.n.back.training.data.entity.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(record: Record)

    @Query("SELECT * FROM record ORDER BY timestamp DESC")
    fun getRecords(): Flow<List<Record>>
}