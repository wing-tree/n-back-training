package com.wing.tree.n.back.training.di

import com.wing.tree.n.back.training.data.datasource.local.record.RecordDataSource
import com.wing.tree.n.back.training.data.datasource.local.record.RecordDataSourceImpl
import com.wing.tree.n.back.training.data.datasource.network.ranking.RankingDataSource
import com.wing.tree.n.back.training.data.datasource.network.ranking.RankingDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsRankingDataSource(dataSource: RankingDataSourceImpl): RankingDataSource

    @Binds
    @Singleton
    abstract fun bindsRecordDataSource(dataSource: RecordDataSourceImpl): RecordDataSource
}