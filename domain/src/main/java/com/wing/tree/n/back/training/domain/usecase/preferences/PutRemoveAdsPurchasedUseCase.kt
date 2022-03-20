package com.wing.tree.n.back.training.domain.usecase.preferences

import com.wing.tree.n.back.training.domain.repository.PreferencesRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PutRemoveAdsPurchasedUseCase @Inject constructor(
    private val repository: PreferencesRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<PutRemoveAdsPurchasedUseCase.Parameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Parameter) {
        return repository.putIsFirstTime(parameter.removeAdsPurchased)
    }

    data class Parameter(val removeAdsPurchased: Boolean)
}