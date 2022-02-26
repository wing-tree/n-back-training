package com.wing.tree.n.back.training.data.repository

import androidx.datastore.core.DataStore
import com.wing.tree.n.back.training.data.datastore.option.Option
import com.wing.tree.n.back.training.data.mapper.OptionMapper.toDomainModel
import com.wing.tree.n.back.training.domain.model.Option as DomainModel
import com.wing.tree.n.back.training.domain.repository.OptionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OptionRepositoryImpl @Inject constructor(private val dataStore: DataStore<Option>) : OptionRepository {
    private val data = dataStore.data

    override suspend fun option(): DomainModel {
        return data.first().toDomainModel()
    }

    override suspend fun update(option: DomainModel) {
        dataStore.updateData {
            it.toBuilder()
                .setRounds(option.rounds)
                .setSpeed(option.speed)
                .setSpeedMode(option.speedMode)
                .build()
        }
    }
}