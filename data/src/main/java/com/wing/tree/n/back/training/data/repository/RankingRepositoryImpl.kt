package com.wing.tree.n.back.training.data.repository

import com.wing.tree.n.back.training.data.datasource.network.ranking.RankingDataSource
import com.wing.tree.n.back.training.data.mapper.RankingMapper.toDataModel
import com.wing.tree.n.back.training.domain.model.Ranking
import com.wing.tree.n.back.training.domain.repository.RankingRepository
import javax.inject.Inject

class RankingRepositoryImpl @Inject constructor(private val dataSource: RankingDataSource) : RankingRepository {
    override suspend fun registerRanking(
        ranking: Ranking,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        dataSource.registerRanking(ranking.toDataModel(), onSuccess, onFailure)
    }
}