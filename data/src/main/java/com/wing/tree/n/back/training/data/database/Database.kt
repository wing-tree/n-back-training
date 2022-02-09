package com.wing.tree.n.back.training.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wing.tree.n.back.training.data.constant.PACKAGE_NAME
import com.wing.tree.n.back.training.data.dao.RecordDao
import com.wing.tree.n.back.training.data.entity.Record
import com.wing.tree.n.back.training.data.typeconverter.TypeConverters

@androidx.room.Database(
    entities = [Record::class],
    exportSchema = false,
    version = 1
)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class Database: RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        private const val CLASS_NAME = "Database"
        private const val NAME = "$PACKAGE_NAME.$CLASS_NAME"
        private const val VERSION = "1.0.0"

        @Volatile
        private var INSTANCE: Database? = null

        fun instance(context: Context): Database {
            synchronized(this) {
                return INSTANCE ?: run {
                    Room.databaseBuilder(
                        context.applicationContext,
                        Database::class.java,
                        "$NAME.$VERSION"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                        .also {
                            INSTANCE = it
                        }
                }
            }
        }
    }
}