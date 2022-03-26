package com.wing.tree.n.back.training.presentation.delegate.billing

import com.android.billingclient.api.Purchase

interface PurchaseCallbacks {
    fun onFailure(debugMessage: String, responseCode: Int) = Unit
    fun onPurchaseAcknowledged(purchase: Purchase) = Unit
    fun onPurchaseConsumed(purchase: Purchase) = Unit
}