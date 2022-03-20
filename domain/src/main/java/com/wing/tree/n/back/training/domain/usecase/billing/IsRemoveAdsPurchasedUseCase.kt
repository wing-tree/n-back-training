package com.wing.tree.n.back.training.domain.usecase.billing

import com.wing.tree.n.back.training.domain.repository.BillingRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import com.wing.tree.n.back.training.domain.usecase.Result
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class IsRemoveAdsPurchasedUseCase @Inject constructor(
    private val repository: BillingRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<IsRemoveAdsPurchasedUseCase.Parameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Parameter) {
        repository.isRemoveAdsPurchased(
            onSuccess = { parameter.onComplete(Result.Success<Boolean>(data = it)) },
            onFailure = { parameter.onComplete(Result.Error(throwable = it)) }
        )
    }

    class Parameter(val onComplete: (Result<Boolean>) -> Unit)
}