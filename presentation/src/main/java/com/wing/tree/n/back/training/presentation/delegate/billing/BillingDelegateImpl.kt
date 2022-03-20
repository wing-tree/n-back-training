package com.wing.tree.n.back.training.presentation.delegate.billing

import android.content.Context
import androidx.annotation.MainThread
import com.android.billingclient.api.*
import com.wing.tree.n.back.training.domain.model.billing.Sku
import com.wing.tree.n.back.training.domain.util.`is`
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingDelegateImpl : BillingDelegate {
    private val consumableSkusList = listOf(Sku.REMOVE_ADS)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
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
                    billingCallback?.onFailure(IllegalStateException("responseCode :${billingResult.responseCode}"))
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
                    billingCallback?.onFailure(responseCode)
                }
            }
        }
    }

    fun querySkuDetails(
        skuType: String = BillingClient.SkuType.INAPP,
        @MainThread onSkuDetailsResult: (SkuDetailsResult) -> Unit
    ) {
        val builder = SkuDetailsParams.newBuilder().apply {
            setSkusList(skusList).setType(skuType)
        }

        coroutineScope.launch {
            billingClient?.let {
                val skuDetailsResult = it.querySkuDetails(builder.build())

                withContext(Dispatchers.Main) {
                    onSkuDetailsResult.invoke(skuDetailsResult)
                }
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

                    coroutineScope.launch {
                        billingClient?.let {
                            val consumeResult = it.consumePurchase(consumeParams)

                            withContext(Dispatchers.Main) {
                                if (consumeResult.billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                                    billingCallback?.onConsumed(purchase)
                                }
                            }
                        }
                    }
                }

                purchase.isAcknowledged.not() -> {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    coroutineScope.launch {
                        billingClient?.let {
                            val billingResult = it.acknowledgePurchase(acknowledgePurchaseParams)

                            withContext(Dispatchers.Main) {
                                if (billingResult.responseCode.`is`(BillingClient.BillingResponseCode.OK)) {
                                    billingCallback?.onAcknowledged(purchase)
                                } else {
                                    billingCallback?.onFailure(IllegalStateException("responseCode :${billingResult.responseCode}"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}