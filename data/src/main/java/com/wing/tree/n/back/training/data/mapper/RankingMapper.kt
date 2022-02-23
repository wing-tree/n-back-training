package com.wing.tree.n.back.training.data.mapper

import com.google.firebase.Timestamp
import com.wing.tree.n.back.training.data.model.Ranking as DataModel
import com.wing.tree.n.back.training.domain.model.Ranking as DomainModel

object RankingMapper {
    fun DomainModel.toDataModel() = DataModel(
        elapsedTime = elapsedTime,
        n = n,
        nation = nation,
        nickname = nickname,
        rounds = rounds,
        timestamp = Timestamp(timestamp)
    )

    fun DataModel.toDomainModel() = DomainModel(
        elapsedTime = elapsedTime,
        n = n,
        nation = nation,
        nickname = nickname,
        rounds = rounds,
        timestamp = timestamp.toDate()
    )
}