package com.wing.tree.n.back.training.domain.usecase.preferences

import com.wing.tree.n.back.training.domain.repository.PreferencesRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetFirstTimeUseCase @Inject constructor(
    private val repository: PreferencesRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, Boolean>(coroutineDispatcher) {
    override suspend fun execute(parameter: Unit): Boolean {
        return repository.getFirstTime()
    }
}