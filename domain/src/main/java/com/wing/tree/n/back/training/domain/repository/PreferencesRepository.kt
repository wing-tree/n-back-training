package com.wing.tree.n.back.training.domain.repository

interface PreferencesRepository {
    suspend fun getFirstTime() : Boolean
    suspend fun getSortBy(): Int
    suspend fun putIsFirstTime(isFirstTime: Boolean)
    suspend fun putSortBy(sortBy: Int)
}