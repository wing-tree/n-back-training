package com.wing.tree.n.back.training.presentation.delegate.billing

import androidx.annotation.MainThread
import com.android.billingclient.api.Purchase

interface BillingCallback {
    fun onAcknowledged(purchase: Purchase)
    fun onBillingSetupFinished()
    fun onConsumed(purchase: Purchase)
    fun onFailure(responseCode: Int)
}