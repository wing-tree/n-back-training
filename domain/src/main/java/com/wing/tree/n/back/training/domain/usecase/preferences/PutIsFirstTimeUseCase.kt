package com.wing.tree.n.back.training.domain.usecase.preferences

import com.wing.tree.n.back.training.domain.repository.PreferencesRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PutIsFirstTimeUseCase @Inject constructor(
    private val repository: PreferencesRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<PutIsFirstTimeUseCase.Parameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Parameter) {
        return repository.putIsFirstTime(parameter.isFirstTime)
    }

    data class Parameter(val isFirstTime: Boolean)
}