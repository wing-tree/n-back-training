package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wing.tree.n.back.training.domain.usecase.Result
import com.wing.tree.n.back.training.domain.usecase.option.GetOptionUseCase
import com.wing.tree.n.back.training.domain.usecase.option.UpdateOptionUseCase
import com.wing.tree.n.back.training.presentation.model.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOptionUseCase: GetOptionUseCase,
    application: Application
) : AndroidViewModel(application) {
    val option: Option get() = runBlocking {
        return@runBlocking when(val option = getOptionUseCase.invoke(Unit).first()) {
            is Result.Error -> Option.Default
            is Result.Loading -> Option.Default
            is Result.Success -> Option.from(option.data)
        }
    }
}