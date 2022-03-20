package com.wing.tree.n.back.training.domain.repository

interface PreferencesRepository {
    suspend fun getFirstTime() : Boolean
    suspend fun getRemoveAdsPurchased(): Boolean
    suspend fun getSortBy(): Int
    suspend fun putIsFirstTime(isFirstTime: Boolean)
    suspend fun putRemoveAdsPurchased(removeAdsPurchased: Boolean)
    suspend fun putSortBy(sortBy: Int)
}