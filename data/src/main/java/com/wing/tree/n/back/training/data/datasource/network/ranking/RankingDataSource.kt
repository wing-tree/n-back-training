package com.wing.tree.n.back.training.data.datasource.network.ranking

import androidx.annotation.MainThread
import com.wing.tree.n.back.training.data.model.Ranking
import com.wing.tree.n.back.training.domain.model.RankCheckParameter

interface RankingDataSource {
    suspend fun checkRanking(
        rankCheckParameter: RankCheckParameter,
        @MainThread
        onSuccess: (Boolean) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    )

    suspend fun getRankingList(
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
        onSuccess: (Int) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    )
}