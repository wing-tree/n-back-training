package com.wing.tree.n.back.training.presentation.delegate.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails

interface BillingDelegate {
    fun build(context: Context)
    fun endConnection()
    fun launchBillingFlow(activity: Activity, skuDetails: SkuDetails)
    fun queryPurchasesAsync(skuType: String = BillingClient.SkuType.INAPP)
    fun querySkuDetails(skuType: String = BillingClient.SkuType.INAPP, onSkuDetailsList: (List<SkuDetails>) -> Unit)
    fun registerPurchaseCallbacks(purchaseCallbacks: PurchaseCallbacks)
    fun startConnection(billingClientStateCallbacks: BillingClientStateCallbacks)
}