package com.wing.tree.n.back.training.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P: Any, R: Any>(private val coroutineDispatcher: CoroutineDispatcher) {
   operator fun invoke(parameter: P): Flow<Result<R>> = execute(parameter)
       .catch { emit(Result.Error(it)) }
       .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameter: P): Flow<Result<R>>
}