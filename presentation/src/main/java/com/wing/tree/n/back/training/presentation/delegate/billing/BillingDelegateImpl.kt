package com.wing.tree.n.back.training.presentation.delegate.billing

import android.content.Context
import androidx.annotation.MainThread
import com.android.billingclient.api.*
import com.wing.tree.n.back.training.domain.model.billing.Sku
import com.wing.tree.n.back.training.domain.util.`is`

class BillingDelegateImpl : BillingDelegate {
    private val consumableSkusList = listOf(Sku.REMOVE_ADS)
    private val skusList = listOf(Sku.REMOVE_ADS)

    private val purchasesUpdatedListener by lazy {
        PurchasesUpdatedListener { billingResult, purchases ->
            purchases?.let {
                val responseCode = billingResult.responseCode

                if (responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                } else {
                    billingCallback?.onFailure(billingResult.responseCode)
                }
            }
        }
    }

    private var billingClient: BillingClient? = null
    private var billingCallback: BillingCallback? = null

    override fun startBillingClientConnection(
        context: Context,
        billingCallback: BillingCallback
    ) {
        if (billingClient?.isReady.`is`(true)) {
            billingClient?.endConnection()
        }

        this.billingClient = null
        this.billingCallback = billingCallback

        val billingClientStateListener = object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                    billingCallback.onBillingSetupFinished()
                } else {
                    billingCallback.onFailure(billingResult.responseCode)
                }
            }

            override fun onBillingServiceDisconnected() {
                this@BillingDelegateImpl.billingCallback = null
                this@BillingDelegateImpl.billingClient = null
            }
        }

        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient?.startConnection(billingClientStateListener)
    }

    fun queryPurchasesAsync(skuType: String = BillingClient.SkuType.INAPP,) {
        billingClient?.let {
            it.queryPurchasesAsync(skuType) { billingResult, purchases ->
                val responseCode = billingResult.responseCode

                if (responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                } else {
                    billingCallback?.onFailure(billingResult.responseCode)
                }
            }
        }
    }

    fun querySkuDetails(
        skuType: String = BillingClient.SkuType.INAPP,
        @MainThread onSkuDetailsList: (List<SkuDetails>) -> Unit
    ) {
        val skuDetailsParams = SkuDetailsParams
            .newBuilder().apply {
                setSkusList(skusList).setType(skuType)
            }
            .build()

        billingClient?.querySkuDetailsAsync(skuDetailsParams) { billingResult, skuDetailsList ->
            if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                skuDetailsList?.let { onSkuDetailsList.invoke(it) }
            } else {
                billingCallback?.onFailure(billingResult.responseCode)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState.`is`(Purchase.PurchaseState.PURCHASED)) {
            when {
                consumableSkusList.containsAll(purchase.skus) -> {
                    val consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient?.consumeAsync(consumeParams) { billingResult, purchaseToken ->
                        if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                            billingCallback?.onConsumed(purchase)
                        } else {
                            billingCallback?.onFailure(billingResult.responseCode)
                        }
                    }
                }

                purchase.isAcknowledged.not() -> {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams
                        .newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                        if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                            billingCallback?.onAcknowledged(purchase)
                        } else {
                            billingCallback?.onFailure(billingResult.responseCode)
                        }
                    }
                }
            }
        }
    }
}