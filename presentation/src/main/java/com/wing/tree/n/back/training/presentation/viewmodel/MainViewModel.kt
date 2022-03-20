package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wing.tree.n.back.training.domain.usecase.Result
import com.wing.tree.n.back.training.domain.usecase.option.GetOptionUseCase
import com.wing.tree.n.back.training.domain.usecase.preferences.GetFirstTimeUseCase
import com.wing.tree.n.back.training.domain.usecase.preferences.GetRemoveAdsPurchased
import com.wing.tree.n.back.training.domain.usecase.preferences.PutIsFirstTimeUseCase
import com.wing.tree.n.back.training.presentation.model.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFirstTimeUseCase: GetFirstTimeUseCase,
    private val getOptionUseCase: GetOptionUseCase,
    private val getRemoveAdsPurchased: GetRemoveAdsPurchased,
    private val putIsFirstTimeUseCase: PutIsFirstTimeUseCase,
    application: Application
) : AndroidViewModel(application) {
    val option: Option get() = runBlocking {
        return@runBlocking withTimeoutOrNull(250L) {
            when (val option = getOptionUseCase.invoke(Unit)) {
                is Result.Error -> Option.Default
                is Result.Loading -> Option.Default
                is Result.Success -> Option.from(option.data)
            }
        } ?: Option.Default
    }

    val isFirstTime: Boolean get() = runBlocking {
        return@runBlocking withTimeoutOrNull(250L) {
            with(getFirstTimeUseCase.invoke(Unit)) {
                when(this) {
                    is Result.Error -> false
                    is Result.Loading -> false
                    is Result.Success -> this.data
                }
            }
        } ?: false
    }

    fun updateIsFirstTimeToFalse() {
        viewModelScope.launch(Dispatchers.IO) {
            putIsFirstTimeUseCase.invoke(PutIsFirstTimeUseCase.Parameter(false))
        }
    }
}