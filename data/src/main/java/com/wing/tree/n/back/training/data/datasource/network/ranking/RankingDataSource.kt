package com.wing.tree.n.back.training.data.datasource.network.ranking

import androidx.annotation.MainThread
import com.wing.tree.n.back.training.data.model.Ranking
import com.wing.tree.n.back.training.domain.model.RankCheckParameter

interface RankingDataSource {
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

    suspend fun registerForRanking(
        ranking: Ranking,
        @MainThread
        onSuccess: () -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    )
}