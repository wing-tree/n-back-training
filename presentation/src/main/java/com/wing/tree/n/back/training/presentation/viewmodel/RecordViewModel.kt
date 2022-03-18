package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wing.tree.n.back.training.domain.model.SortBy
import com.wing.tree.n.back.training.domain.usecase.Result
import com.wing.tree.n.back.training.domain.usecase.preferences.GetSortByUseCase
import com.wing.tree.n.back.training.domain.usecase.preferences.PutSortByUseCase
import com.wing.tree.n.back.training.domain.usecase.record.DeleteRecordUseCase
import com.wing.tree.n.back.training.domain.usecase.record.GetRecordsUseCase
import com.wing.tree.n.back.training.presentation.model.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    getRecordsUseCase: GetRecordsUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val getSortByUseCase: GetSortByUseCase,
    private val putSortByUseCase: PutSortByUseCase,
    application: Application
) : AndroidViewModel(application) {
    val records = getRecordsUseCase.invoke(Unit).map {
        when(it) {
            is Result.Error -> {
                Timber.e(it.throwable)
                emptyList()
            }
            is Result.Loading -> emptyList()
            is Result.Success -> it.data
        }
    }.map { list ->
        list.map { Record.from(it) }
    }.asLiveData(viewModelScope.coroutineContext)

    val sortBy: SortBy
        get() = SortBy.valueOf(
            runBlocking {
                withTimeoutOrNull(250L) {
                    when(val sortBy = getSortByUseCase.invoke(Unit)) {
                        is Result.Error -> SortBy.Default
                        is Result.Loading -> SortBy.Default
                        is Result.Success -> sortBy.data
                    }
                } ?: SortBy.Default
            }
        )

    fun putSortBy(sortBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            putSortByUseCase.invoke(PutSortByUseCase.Parameter(sortBy))
        }
    }

    fun delete(record: Record) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRecordUseCase.invoke(DeleteRecordUseCase.Parameter(record))
        }
    }
}