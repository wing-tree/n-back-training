package com.wing.tree.n.back.training.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.wing.tree.n.back.training.data.constant.PACKAGE_NAME
import com.wing.tree.n.back.training.domain.model.SortBy
import com.wing.tree.n.back.training.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : PreferencesRepository {
    private object Name {
        private const val OBJECT_NAME = "Name"
        const val FIRST_TIME = "$PACKAGE_NAME.$OBJECT_NAME.FIRST_TIME"
        const val SORT_BY = "$PACKAGE_NAME.$OBJECT_NAME.SORT_BY"
    }

    private object Key {
        val firstTime = booleanPreferencesKey(Name.FIRST_TIME)
        val sortBy = intPreferencesKey(Name.SORT_BY)
    }

    override suspend fun getFirstTime(): Boolean {
        return dataStore.data.map { it[Key.firstTime] }.first() ?: true
    }

    override suspend fun getSortBy(): Int {
        return dataStore.data.map { it[Key.sortBy] }.first() ?: SortBy.Default
    }

    override suspend fun putIsFirstTime(isFirstTime: Boolean) {
        dataStore.edit {
            it[Key.firstTime] = isFirstTime
        }
    }

    override suspend fun putSortBy(sortBy: Int) {
        dataStore.edit {
            it[Key.sortBy] = sortBy
        }
    }
}