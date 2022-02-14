package com.wing.tree.n.back.training.domain.usecase.option

import com.wing.tree.n.back.training.domain.model.Option
import com.wing.tree.n.back.training.domain.repository.OptionRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.MainCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetOptionUseCase @Inject constructor(
    private val repository: OptionRepository,
    @MainCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, Option>(coroutineDispatcher) {
    override suspend fun execute(parameter: Unit): Option {
        return repository.option()
    }
}