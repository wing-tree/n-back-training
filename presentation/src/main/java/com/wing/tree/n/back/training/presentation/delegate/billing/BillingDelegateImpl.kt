package com.wing.tree.n.back.training.presentation.delegate.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.wing.tree.n.back.training.domain.util.`is`
import com.wing.tree.n.back.training.presentation.constant.Sku
import timber.log.Timber

object BillingDelegateImpl : BillingDelegate {
    private val consumableSkusList = listOf<String>()
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
                    purchaseCallbacks?.onFailure(billingResult.debugMessage, billingResult.responseCode)
                }
            }
        }
    }

    private var billingClient: BillingClient? = null
    private var purchaseCallbacks: PurchaseCallbacks? = null

    private fun acknowledgePurchase(purchase: Purchase) {
        verifyPurchase(purchase) {
            if (purchase.isAcknowledged.not()) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                        purchaseCallbacks?.onPurchaseAcknowledged(purchase)
                    } else {
                        purchaseCallbacks?.onFailure(
                            billingResult.debugMessage,
                            billingResult.responseCode
                        )
                    }
                }
            } else {
                purchaseCallbacks?.onPurchaseAcknowledged(purchase)
            }
        }
    }

    override fun build(context: Context) {
        billingClient = BillingClient
            .newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    private fun consumePurchase(purchase: Purchase) {
        verifyPurchase(purchase) {
            if (purchase.hasConsumableSkus) {
                val consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient?.consumeAsync(consumeParams) { billingResult, _ ->
                    if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                        purchaseCallbacks?.onPurchaseConsumed(purchase)
                    } else {
                        purchaseCallbacks?.onFailure(
                            billingResult.debugMessage,
                            billingResult.responseCode
                        )
                    }
                }
            } else {
                val message = "purchase.hasConsumableSkus :${purchase.hasConsumableSkus}"

                Timber.d(message)
            }
        }
    }

    private fun clear() {
        billingClient = null
        purchaseCallbacks = null
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.isPurchased) {
            if (purchase.hasConsumableSkus) {
                consumePurchase(purchase)
            } else {
                acknowledgePurchase(purchase)
            }
        } else {
            Timber.d("purchase :$purchase")
        }
    }

    private fun verifyPurchase(purchase: Purchase, onPurchaseVerified: (Purchase) -> Unit) {
        onPurchaseVerified(purchase)
    }

    override fun endConnection() {
        billingClient?.endConnection()
        clear()
    }

    override fun launchBillingFlow(activity: Activity, skuDetails: SkuDetails) {
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
                    purchaseCallbacks?.onFailure(billingResult.debugMessage, responseCode)
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
                purchaseCallbacks?.onFailure(billingResult.debugMessage, billingResult.responseCode)
            }
        }
    }

    override fun registerPurchaseCallbacks(purchaseCallbacks: PurchaseCallbacks) {
        this.purchaseCallbacks = purchaseCallbacks
    }

    override fun startConnection(billingClientStateCallbacks: BillingClientStateCallbacks) {
        val billingClientStateListener = object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                    billingClientStateCallbacks.onBillingSetupFinished()
                } else {
                    billingClientStateCallbacks.onFailure(billingResult.debugMessage, billingResult.responseCode)
                }
            }

            override fun onBillingServiceDisconnected() {
                clear()
            }
        }

        billingClient?.startConnection(billingClientStateListener)
    }

    private val Purchase.hasConsumableSkus: Boolean get() = consumableSkusList.containsAll(skus)
    private val Purchase.isPurchased: Boolean get() = purchaseState.`is`(Purchase.PurchaseState.PURCHASED)
}