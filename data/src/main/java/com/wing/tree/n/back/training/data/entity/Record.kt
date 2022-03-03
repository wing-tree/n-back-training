package com.wing.tree.n.back.training.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wing.tree.n.back.training.domain.model.Record

@Entity(tableName = "record")
data class Record(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0L,
    override val elapsedTime: Long,
    override val n: Int,
    override val problems: List<Problem>,
    override val rounds: Int,
    override val speed: Int,
    override val timestamp: Long
) : Record()