package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.wing.tree.n.back.training.presentation.constant.Sku
import com.wing.tree.n.back.training.presentation.delegate.billing.BillingCallback
import com.wing.tree.n.back.training.presentation.delegate.billing.BillingDelegate
import com.wing.tree.n.back.training.presentation.delegate.billing.BillingDelegateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application), BillingDelegate by BillingDelegateImpl {
    private val billingCallback by lazy {
        object : BillingCallback {
            override fun onFailure(responseCode: Int) {
                Timber.e("responseCode :$responseCode")
            }

            override fun onPurchaseConsumed(purchase: Purchase) {
                if (purchase.skus.contains(Sku.REMOVE_ADS)) {
                    Timber.d("remove_ads purchased")
                }
            }
        }
    }

    private val _skuDetailsList = MutableLiveData<List<SkuDetails>>()
    val skuDetailsList: LiveData<List<SkuDetails>> get() = _skuDetailsList

    init {
        setCallback(billingCallback)
        querySkuDetails { _skuDetailsList.postValue(it) }
    }
}