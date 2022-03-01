package com.wing.tree.n.back.training.domain.repository

import androidx.annotation.MainThread
import com.wing.tree.n.back.training.domain.model.RankCheckParameter
import com.wing.tree.n.back.training.domain.model.Ranking

interface RankingRepository {
    suspend fun checkRanking(
        rankCheckParameter: RankCheckParameter,
        @MainThread
        onSuccess: (Boolean, Int) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    )

    suspend fun getRankings(
        page: Int,
        pageSize: Long,
        @MainThread
        onSuccess: (List<Ranking>) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    )

    suspend fun registerRanking(
        ranking: Ranking,
        @MainThread
        onSuccess: () -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    )
}