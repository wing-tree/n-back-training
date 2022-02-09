package com.wing.tree.n.back.training.domain.usecase.option

import com.wing.tree.n.back.training.domain.model.Option
import com.wing.tree.n.back.training.domain.repository.OptionRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateOptionUseCase @Inject constructor(
    private val repository: OptionRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<Option, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Option) {
        repository.update(parameter)
    }
}