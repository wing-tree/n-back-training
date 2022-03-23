package com.wing.tree.n.back.training.presentation.delegate.billing

import com.android.billingclient.api.Purchase

interface BillingCallback {
    fun onBillingSetupFinished() = Unit
    fun onFailure(responseCode: Int)
    fun onPurchaseAcknowledged(purchase: Purchase) = Unit
    fun onPurchaseConsumed(purchase: Purchase) = Unit
}