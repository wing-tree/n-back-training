package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.wing.tree.n.back.training.presentation.constant.Sku
import com.wing.tree.n.back.training.presentation.delegate.billing.BillingClientStateCallbacks
import com.wing.tree.n.back.training.presentation.delegate.billing.BillingDelegate
import com.wing.tree.n.back.training.presentation.delegate.billing.BillingDelegateImpl
import com.wing.tree.n.back.training.presentation.delegate.billing.PurchaseCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application), BillingDelegate by BillingDelegateImpl {
    private val purchaseCallbacks by lazy {
        object : PurchaseCallbacks {
            override fun onFailure(debugMessage: String, responseCode: Int) {
                Timber.e("debugMessage: $debugMessage, responseCode :$responseCode")
            }

            override fun onPurchaseAcknowledged(purchase: Purchase) {
                if (purchase.skus.contains(Sku.REMOVE_ADS)) {
                    Timber.d("remove_ads purchased")
                }
            }
        }
    }

    init {
        registerPurchaseCallbacks(purchaseCallbacks)
        querySkuDetails { _skuDetailsList.postValue(it) }
    }

    private val _skuDetailsList = MutableLiveData<List<SkuDetails>>()
    val skuDetailsList: LiveData<List<SkuDetails>> get() = _skuDetailsList
}