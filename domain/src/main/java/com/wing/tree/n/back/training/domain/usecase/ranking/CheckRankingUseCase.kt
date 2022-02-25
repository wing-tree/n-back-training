package com.wing.tree.n.back.training.domain.usecase.ranking

import androidx.annotation.MainThread
import com.wing.tree.n.back.training.domain.model.RankCheckParameter
import com.wing.tree.n.back.training.domain.model.Ranking
import com.wing.tree.n.back.training.domain.repository.RankingRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CheckRankingUseCase @Inject constructor(
    private val repository: RankingRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<CheckRankingUseCase.Parameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Parameter) {
        with(parameter) {
            repository.checkRanking(
                rankCheckParameter,
                onSuccess,
                onFailure
            )
        }
    }

    class Parameter(
        val rankCheckParameter: RankCheckParameter,
        @MainThread
        val onSuccess: (Boolean) -> Unit,
        @MainThread
        val onFailure: (Exception) -> Unit
    )
}