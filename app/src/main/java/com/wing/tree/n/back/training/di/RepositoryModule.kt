package com.wing.tree.n.back.training.di

import com.wing.tree.n.back.training.data.repository.OptionRepositoryImpl
import com.wing.tree.n.back.training.data.repository.PreferencesRepositoryImpl
import com.wing.tree.n.back.training.data.repository.RankingRepositoryImpl
import com.wing.tree.n.back.training.data.repository.RecordRepositoryImpl
import com.wing.tree.n.back.training.domain.repository.OptionRepository
import com.wing.tree.n.back.training.domain.repository.PreferencesRepository
import com.wing.tree.n.back.training.domain.repository.RankingRepository
import com.wing.tree.n.back.training.domain.repository.RecordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsOptionRepository(repository: OptionRepositoryImpl): OptionRepository

    @Binds
    @Singleton
    abstract fun bindsPreferencesRepository(repository: PreferencesRepositoryImpl): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindsRankingRepository(repository: RankingRepositoryImpl): RankingRepository

    @Binds
    @Singleton
    abstract fun bindsRecordRepository(repository: RecordRepositoryImpl): RecordRepository
}