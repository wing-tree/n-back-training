package com.wing.tree.n.back.training.data.datasource.network.ranking

import androidx.annotation.MainThread
import com.wing.tree.n.back.training.data.model.Ranking

interface RankingDataSource {
    suspend fun checkRanking(
        ranking: Ranking,
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