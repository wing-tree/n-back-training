package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wing.tree.n.back.training.domain.usecase.Result
import com.wing.tree.n.back.training.domain.usecase.option.GetOptionUseCase
import com.wing.tree.n.back.training.domain.usecase.preferences.GetFirstTimeUseCase
import com.wing.tree.n.back.training.domain.usecase.preferences.GetRemoveAdsPurchased
import com.wing.tree.n.back.training.domain.usecase.preferences.PutIsFirstTimeUseCase
import com.wing.tree.n.back.training.domain.usecase.preferences.PutRemoveAdsPurchasedUseCase
import com.wing.tree.n.back.training.presentation.model.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFirstTimeUseCase: GetFirstTimeUseCase,
    private val getOptionUseCase: GetOptionUseCase,
    private val getRemoveAdsPurchased: GetRemoveAdsPurchased,
    private val putIsFirstTimeUseCase: PutIsFirstTimeUseCase,
    private val putRemoveAdsPurchasedUseCase: PutRemoveAdsPurchasedUseCase,
    application: Application
) : AndroidViewModel(application) {
    private val _adsRemoved = MutableLiveData<Boolean>()
    val adsRemoved: LiveData<Boolean> get() = _adsRemoved

    val isFirstTime: Boolean get() = runBlocking {
        return@runBlocking withTimeoutOrNull(120L) {
            with(getFirstTimeUseCase.invoke(Unit)) {
                when(this) {
                    is Result.Error -> false
                    is Result.Loading -> false
                    is Result.Success -> this.data
                }
            }
        } ?: false
    }

    val option: Option get() = runBlocking {
        return@runBlocking withTimeoutOrNull(120L) {
            when (val option = getOptionUseCase.invoke(Unit)) {
                is Result.Error -> Option.Default
                is Result.Loading -> Option.Default
                is Result.Success -> Option.from(option.data)
            }
        } ?: Option.Default
    }

    val removeAdsPurchased = runBlocking {
        return@runBlocking withTimeoutOrNull(120L) {
            with(getRemoveAdsPurchased.invoke(Unit)) {
                when(this) {
                    is Result.Error -> false
                    is Result.Loading -> false
                    is Result.Success -> this.data
                }
            }
        } ?: false
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            with(getRemoveAdsPurchased.invoke(Unit)) {
                val value = when(this) {
                    is Result.Error -> false
                    is Result.Loading -> false
                    is Result.Success -> this.data
                }

                withContext(Dispatchers.Main) {
                    _adsRemoved.value = value
                }
            }
        }
    }

    fun notifyAdsRemoved() {
//        viewModelScope.launch(Dispatchers.IO) {
//            putRemoveAdsPurchasedUseCase.invoke(PutRemoveAdsPurchasedUseCase.Parameter(true))
//        }

        _adsRemoved.postValue(true)
    }

    fun updateIsFirstTimeToFalse() {
        viewModelScope.launch(Dispatchers.IO) {
            putIsFirstTimeUseCase.invoke(PutIsFirstTimeUseCase.Parameter(false))
        }
    }
}