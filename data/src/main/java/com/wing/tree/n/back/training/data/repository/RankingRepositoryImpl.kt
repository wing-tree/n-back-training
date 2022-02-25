package com.wing.tree.n.back.training.data.repository

import com.wing.tree.n.back.training.data.datasource.network.ranking.RankingDataSource
import com.wing.tree.n.back.training.data.mapper.RankingMapper.toDataModel
import com.wing.tree.n.back.training.data.mapper.RankingMapper.toDomainModel
import com.wing.tree.n.back.training.domain.model.Ranking
import com.wing.tree.n.back.training.domain.repository.RankingRepository
import javax.inject.Inject

class RankingRepositoryImpl @Inject constructor(private val dataSource: RankingDataSource) : RankingRepository {
    override suspend fun checkRanking(
        ranking: Ranking,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        dataSource.checkRanking(ranking.toDataModel(), onSuccess, onFailure)
    }

    override suspend fun getRankings(
        page: Int,
        pageSize: Long,
        onSuccess: (List<Ranking>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        dataSource.getRankingList(
            page,
            pageSize,
            onSuccess = { list ->
                onSuccess(list.map{ it.toDomainModel() })
            },
            onFailure = onFailure
        )
    }

    override suspend fun registerRanking(
        ranking: Ranking,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        dataSource.registerForRanking(ranking.toDataModel(), onSuccess, onFailure)
    }
}