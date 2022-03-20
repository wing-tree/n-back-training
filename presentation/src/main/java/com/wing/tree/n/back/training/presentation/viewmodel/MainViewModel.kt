package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wing.tree.n.back.training.domain.model.billing.BillingClientStateCallback
import com.wing.tree.n.back.training.domain.model.billing.SkuDetails
import com.wing.tree.n.back.training.domain.usecase.Result
import com.wing.tree.n.back.training.domain.usecase.billing.EndBillingClientConnectionUseCase
import com.wing.tree.n.back.training.domain.usecase.billing.IsRemoveAdsPurchasedUseCase
import com.wing.tree.n.back.training.domain.usecase.billing.StartBillingClientConnectionUseCase
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val endBillingClientConnectionUseCase: EndBillingClientConnectionUseCase,
    private val getFirstTimeUseCase: GetFirstTimeUseCase,
    private val getOptionUseCase: GetOptionUseCase,
    private val getRemoveAdsPurchased: GetRemoveAdsPurchased,
    private val isRemoveAdsPurchasedUseCase: IsRemoveAdsPurchasedUseCase,
    private val putIsFirstTimeUseCase: PutIsFirstTimeUseCase,
    private val startBillingClientConnectionUseCase: StartBillingClientConnectionUseCase,
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

    val removeAdsPurchased: Boolean get() = runBlocking {
        return@runBlocking withTimeoutOrNull(250L) {
            with(getRemoveAdsPurchased.invoke(Unit)) {
                when(this) {
                    is Result.Error -> false
                    is Result.Loading -> false
                    is Result.Success -> this.data
                }
            }
        } ?: false
    }

    private val _removeAds = MutableLiveData<Boolean>()
    val removeAds: LiveData<Boolean> get() = _removeAds

    init {
        if (removeAdsPurchased) {
            _removeAds.value = true
        } else {
            checkRemoveAdsPurchased()
        }
    }

    private fun endBillingClientConnection() {
        viewModelScope.launch {
            endBillingClientConnectionUseCase.invoke(Unit)
        }
    }

    fun checkRemoveAdsPurchased() = viewModelScope.launch {
        val billingClientStateCallback = object : BillingClientStateCallback {
            override fun onBillingSetupFinished(skuDetailsList: List<SkuDetails>) {
                viewModelScope.launch {
                    isRemoveAdsPurchasedUseCase.invoke(IsRemoveAdsPurchasedUseCase.Parameter {
                        when(it) {
                            is Result.Error, is Result.Loading -> {
                                endBillingClientConnection()
                                _removeAds.postValue(removeAdsPurchased)
                            }
                            is Result.Success -> { _removeAds.postValue(it.data) }
                        }
                    })

                    endBillingClientConnection()
                }
            }

            override fun onFailure(exception: Exception) {
                Timber.e(exception)
                endBillingClientConnection()
                _removeAds.postValue(removeAdsPurchased)
            }

            override fun onFailure(responseCode: Int) {
                Timber.e("responseCode :$responseCode")
                endBillingClientConnection()
                _removeAds.postValue(removeAdsPurchased)
            }
        }

        val parameter = StartBillingClientConnectionUseCase.Parameter(billingClientStateCallback)

        startBillingClientConnectionUseCase.invoke(parameter)
    }

    fun updateIsFirstTimeToFalse() {
        viewModelScope.launch(Dispatchers.IO) {
            putIsFirstTimeUseCase.invoke(PutIsFirstTimeUseCase.Parameter(false))
        }
    }
}