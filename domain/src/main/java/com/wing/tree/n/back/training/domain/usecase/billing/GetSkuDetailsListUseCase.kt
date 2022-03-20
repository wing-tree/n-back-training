package com.wing.tree.n.back.training.domain.usecase.billing

import com.wing.tree.n.back.training.domain.model.billing.SkuDetails
import com.wing.tree.n.back.training.domain.repository.BillingRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import com.wing.tree.n.back.training.domain.usecase.Result
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetSkuDetailsListUseCase @Inject constructor(
    private val repository: BillingRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<GetSkuDetailsListUseCase.Parameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Parameter) {
        return repository.getSkuDetailsList(
            onSuccess = {
                parameter.onSuccess(Result.Success(it))
            },
            onFailure = {
                parameter.onFailure(Result.Error(it))
            }
        )
    }

    class Parameter(
        val onSuccess: (Result.Success<List<SkuDetails>>) -> Unit,
        val onFailure: (Result.Error) -> Unit
    )
}