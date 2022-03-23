package com.wing.tree.n.back.training.presentation.delegate.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails

interface BillingDelegate {
    fun build(context: Context, billingCallback: BillingCallback)
    fun clear()
    fun endConnection()
    fun queryPurchasesAsync(skuType: String = BillingClient.SkuType.INAPP)
    fun querySkuDetails(skuType: String = BillingClient.SkuType.INAPP, onSkuDetailsList: (List<SkuDetails>) -> Unit)
    fun startConnection()
}