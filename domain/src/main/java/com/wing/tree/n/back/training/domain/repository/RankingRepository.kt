package com.wing.tree.n.back.training.domain.repository

import androidx.annotation.MainThread
import com.wing.tree.n.back.training.domain.model.Ranking

interface RankingRepository {
    suspend fun registerRanking(
        ranking: Ranking,
        @MainThread
        onSuccess: (Int) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    )
}