package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wing.tree.n.back.training.domain.usecase.Result
import com.wing.tree.n.back.training.domain.usecase.record.GetRecordListUseCase
import com.wing.tree.n.back.training.presentation.model.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getRecordListUseCase: GetRecordListUseCase,
    application: Application
) : AndroidViewModel(application) {

    val recordList = getRecordListUseCase.invoke(Unit).map {
        when(it) {
            is Result.Error -> emptyList()
            is Result.Loading -> emptyList()
            is Result.Success -> it.data
        }
    }.map { list ->
        list.map { Record.from(it) }
    }.asLiveData(viewModelScope.coroutineContext)
}