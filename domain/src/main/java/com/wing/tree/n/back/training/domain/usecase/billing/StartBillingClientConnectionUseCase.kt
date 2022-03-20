package com.wing.tree.n.back.training.domain.usecase.billing

import com.wing.tree.n.back.training.domain.model.billing.BillingClientStateCallback
import com.wing.tree.n.back.training.domain.repository.BillingRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class StartBillingClientConnectionUseCase @Inject constructor(
    private val repository: BillingRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<StartBillingClientConnectionUseCase.Parameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Parameter) {
        repository.startConnection(parameter.billingClientStateCallback)
    }

    class Parameter(val billingClientStateCallback: BillingClientStateCallback)
}