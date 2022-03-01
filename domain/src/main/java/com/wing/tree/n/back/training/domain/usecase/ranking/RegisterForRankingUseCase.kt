package com.wing.tree.n.back.training.domain.usecase.ranking

import androidx.annotation.MainThread
import com.wing.tree.n.back.training.domain.model.Ranking
import com.wing.tree.n.back.training.domain.repository.RankingRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RegisterForRankingUseCase @Inject constructor(
    private val repository: RankingRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<RegisterForRankingUseCase.Parameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Parameter) {
        repository.registerRanking(
            parameter.ranking,
            parameter.onSuccess,
            parameter.onFailure
        )
    }

    data class Parameter(
        val ranking: Ranking,
        @MainThread
        val onSuccess: () -> Unit,
        @MainThread
        val onFailure: (Exception) -> Unit
    )
}