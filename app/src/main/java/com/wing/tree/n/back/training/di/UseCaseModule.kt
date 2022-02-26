package com.wing.tree.n.back.training.di

import com.wing.tree.n.back.training.domain.repository.OptionRepository
import com.wing.tree.n.back.training.domain.repository.PreferencesRepository
import com.wing.tree.n.back.training.domain.repository.RankingRepository
import com.wing.tree.n.back.training.domain.repository.RecordRepository
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import com.wing.tree.n.back.training.domain.usecase.option.GetOptionUseCase
import com.wing.tree.n.back.training.domain.usecase.option.UpdateOptionUseCase
import com.wing.tree.n.back.training.domain.usecase.preferences.GetSortByUseCase
import com.wing.tree.n.back.training.domain.usecase.preferences.PutSortByUseCase
import com.wing.tree.n.back.training.domain.usecase.ranking.CheckRankingUseCase
import com.wing.tree.n.back.training.domain.usecase.ranking.GetRankingsUseCase
import com.wing.tree.n.back.training.domain.usecase.ranking.RegisterForRankingUseCase
import com.wing.tree.n.back.training.domain.usecase.record.GetRecordsUseCase
import com.wing.tree.n.back.training.domain.usecase.record.InsertRecordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ViewModelComponent::class)
@Module
internal object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun providesCheckRankingUseCase(
        repository: RankingRepository,
        @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
    ): CheckRankingUseCase {
        return CheckRankingUseCase(repository, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun providesGetOptionUseCase(
        repository: OptionRepository,
        @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
    ): GetOptionUseCase {
        return GetOptionUseCase(repository, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun providesGetRankingsUseCase(
        repository: RankingRepository,
        @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
    ): GetRankingsUseCase {
        return GetRankingsUseCase(repository, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun providesGetRecordsUseCase(
        repository: RecordRepository,
        @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
    ): GetRecordsUseCase {
        return GetRecordsUseCase(repository, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun providesGetSortByUseCase(
        repository: PreferencesRepository,
        @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
    ): GetSortByUseCase {
        return GetSortByUseCase(repository, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun providesInsertRecordUseCase(
        repository: RecordRepository,
        @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
    ): InsertRecordUseCase {
        return InsertRecordUseCase(repository, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun providesPutSortByUseCase(
        repository: PreferencesRepository,
        @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
    ): PutSortByUseCase {
        return PutSortByUseCase(repository, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun providesRegisterForRankingUseCase(
        repository: RankingRepository,
        @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
    ): RegisterForRankingUseCase {
        return RegisterForRankingUseCase(repository, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun providesUpdateOptionUseCase(
        repository: OptionRepository,
        @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
    ): UpdateOptionUseCase {
        return UpdateOptionUseCase(repository, coroutineDispatcher)
    }
}