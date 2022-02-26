package com.wing.tree.n.back.training.domain.repository

interface PreferencesRepository {
    suspend fun getSortBy(): Int
    suspend fun putSortBy(sortBy: Int)
}