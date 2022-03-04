package com.wing.tree.n.back.training.domain.usecase.record

import com.wing.tree.n.back.training.domain.model.Record
import com.wing.tree.n.back.training.domain.repository.RecordRepository
import com.wing.tree.n.back.training.domain.usecase.CoroutineUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val repository: RecordRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<DeleteRecordUseCase.Parameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Parameter) {
        repository.delete(parameter.record)
    }

    data class Parameter(val record: Record)
}