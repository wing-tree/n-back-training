package com.wing.tree.n.back.training.domain.usecase.record

import com.wing.tree.n.back.training.domain.model.Record
import com.wing.tree.n.back.training.domain.repository.RecordRepository
import com.wing.tree.n.back.training.domain.usecase.FlowUseCase
import com.wing.tree.n.back.training.domain.usecase.IOCoroutineDispatcher
import com.wing.tree.n.back.training.domain.usecase.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecordListUseCase @Inject constructor(
    private val repository: RecordRepository,
    @IOCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Record>>(coroutineDispatcher) {
    override fun execute(parameter: Unit): Flow<Result<List<Record>>> {
        return repository.getRecordList()
            .map { Result.Success(it) }
            .catch { Result.Error(it) }
    }
}