package com.wing.tree.n.back.training.data.mapper

import com.google.firebase.Timestamp
import com.wing.tree.n.back.training.data.model.Ranking as DataModel
import com.wing.tree.n.back.training.domain.model.Ranking as DomainModel

object RankingMapper {
    fun DomainModel.toDataModel() = DataModel(
        country = country,
        elapsedTime = elapsedTime,
        id = id,
        n = n,
        name = name,
        rounds = rounds,
        speed = speed,
        timestamp = Timestamp(timestamp)
    )

    fun DataModel.toDomainModel() = DomainModel(
        country = country,
        elapsedTime = elapsedTime,
        id = id,
        n = n,
        name = name,
        rounds = rounds,
        speed = speed,
        timestamp = timestamp.toDate()
    )
}