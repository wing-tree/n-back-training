package com.wing.tree.n.back.training.domain.usecase.option

import com.wing.tree.n.back.training.domain.model.Option
import com.wing.tree.n.back.training.domain.repository.OptionRepository
import com.wing.tree.n.back.training.domain.usecase.FlowUseCase
import com.wing.tree.n.back.training.domain.usecase.MainCoroutineDispatcher
import com.wing.tree.n.back.training.domain.usecase.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOptionUseCase @Inject constructor(
    private val repository: OptionRepository,
    @MainCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Option>(coroutineDispatcher) {
    override fun execute(parameter: Unit): Flow<Result<Option>> {
        return repository.option()
            .map { Result.Success(it) }
            .catch { Result.Error(it) }
    }
}