package com.wing.tree.n.back.training.data.repository

import android.content.Context
import com.wing.tree.n.back.training.data.datastore.optionDataStore
import com.wing.tree.n.back.training.data.mapper.OptionMapper.toModel
import com.wing.tree.n.back.training.domain.model.Option
import com.wing.tree.n.back.training.domain.repository.OptionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OptionRepositoryImpl @Inject constructor(@ApplicationContext context: Context) : OptionRepository {
    private val dataStore = context.optionDataStore
    private val data = dataStore.data

    override suspend fun option(): Option {
        return data.first().toModel()
    }

    override suspend fun update(option: Option) {
        dataStore.updateData {
            it.toBuilder()
                .setN(option.n)
                .setRounds(option.rounds)
                .setSpeed(option.speed)
                .build()
        }
    }
}