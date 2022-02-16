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

    @Query("SELECT back FROM record")
    fun backList(): Flow<List<Int>>

    @Query("SELECT * FROM record WHERE back = :back ORDER BY time DESC")
    fun recordList(back: Int): Flow<List<Record>>
}