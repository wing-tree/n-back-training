package com.wing.tree.n.back.training.presentation.delegate.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.wing.tree.n.back.training.domain.util.`is`
import com.wing.tree.n.back.training.presentation.constant.Sku
import timber.log.Timber

object BillingDelegateImpl : BillingDelegate {
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

    override fun build(context: Context, billingCallback: BillingCallback) {
        if (billingClient?.isReady.`is`(true)) {
            billingClient?.endConnection()
        }

        this.billingClient = null
        this.billingCallback = billingCallback

        billingClient = BillingClient
            .newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    override fun clear() {
        billingCallback = null
        billingClient = null
    }

    override fun endConnection() {
        billingClient?.endConnection()
    }

    override fun purchase(activity: Activity, skuDetails: SkuDetails) {
        val billingFlowParams = BillingFlowParams
            .newBuilder()
            .setSkuDetails(skuDetails)
            .build()

        billingClient?.launchBillingFlow(activity, billingFlowParams)
    }

    override fun queryPurchasesAsync(skuType: String) {
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

    override fun querySkuDetails(
        skuType: String,
        onSkuDetailsList: (List<SkuDetails>) -> Unit
    ) {
        val skuDetailsParams = SkuDetailsParams
            .newBuilder().apply {
                setSkusList(skusList).setType(skuType)
            }
            .build()

        billingClient?.querySkuDetailsAsync(skuDetailsParams) { billingResult, skuDetailsList ->
            if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                onSkuDetailsList.invoke(skuDetailsList ?: emptyList())
            } else {
                billingCallback?.onFailure(billingResult.responseCode)
            }
        }
    }

    override fun setCallback(callback: BillingCallback) {
        this.billingCallback = callback
    }

    override fun startConnection() {
        val billingClientStateListener = object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                    billingCallback?.onBillingSetupFinished()
                } else {
                    billingCallback?.onFailure(billingResult.responseCode)
                }
            }

            override fun onBillingServiceDisconnected() {
                clear()
            }
        }

        billingClient?.startConnection(billingClientStateListener)
    }

    private fun handlePurchase(purchase: Purchase) {
        Timber.d("purchase :$purchase")

        if (purchase.purchaseState.`is`(Purchase.PurchaseState.PURCHASED)) {
            when {
                consumableSkusList.containsAll(purchase.skus) -> {
                    val consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient?.consumeAsync(consumeParams) { billingResult, _ ->
                        if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                            billingCallback?.onPurchaseConsumed(purchase)
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
                            billingCallback?.onPurchaseAcknowledged(purchase)
                        } else {
                            billingCallback?.onFailure(billingResult.responseCode)
                        }
                    }
                }
            }
        }
    }
}