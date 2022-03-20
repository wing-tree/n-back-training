package com.wing.tree.n.back.training.domain.usecase.billing

import com.wing.tree.n.back.training.domain.repository.BillingRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EndBillingClientConnectionUseCase @Inject constructor(
    private val repository: BillingRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Unit) {
        repository.endConnection()
    }
}