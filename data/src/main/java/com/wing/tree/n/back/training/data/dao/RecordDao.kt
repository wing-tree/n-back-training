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

    @Query("SELECT n FROM record")
    fun nList(): Flow<List<Int>>

    @Query("SELECT * FROM record WHERE n = :n ORDER BY time DESC")
    fun recordList(n: Int): Flow<List<Record>>
}