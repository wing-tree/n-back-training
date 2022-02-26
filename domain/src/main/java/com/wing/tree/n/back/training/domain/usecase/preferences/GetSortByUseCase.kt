package com.wing.tree.n.back.training.domain.usecase.preferences

import com.wing.tree.n.back.training.domain.repository.PreferencesRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetSortByUseCase @Inject constructor(
    private val repository: PreferencesRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, Int>(coroutineDispatcher) {
    override suspend fun execute(parameter: Unit): Int {
        return repository.getSortBy()
    }
}