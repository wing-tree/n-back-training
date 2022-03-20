package com.wing.tree.n.back.training.data.billing

import android.content.Context
import androidx.annotation.MainThread
import com.android.billingclient.api.*
import com.wing.tree.n.back.training.domain.model.billing.BillingClientStateCallback
import com.wing.tree.n.back.training.domain.model.billing.Sku
import com.wing.tree.n.back.training.domain.model.billing.SkuDetails
import com.wing.tree.n.back.training.domain.util.`is`
import com.wing.tree.n.back.training.domain.util.not
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BillingModule @Inject constructor(@ApplicationContext context: Context) {
    private val consumableSkuList = emptyList<String>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val skusList = listOf(Sku.REMOVE_ADS)

    private val purchasesUpdatedListener by lazy {
        PurchasesUpdatedListener { billingResult, purchases ->
            purchases?.let {
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        for (purchase in purchases) {
                            handlePurchase(purchase)
                        }
                    }
                    else -> {
                        billingClientStateCallback?.onFailure(billingResult.responseCode)
                    }
                }
            }
        }
    }

    private val billingClient by lazy {
        BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    private val billingClientStateListener = object : BillingClientStateListener {
        override fun onBillingSetupFinished(billingResult: BillingResult) {
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                coroutineScope.launch {
                    querySkuDetails { skuDetailsResult ->
                        val skuDetailsList = skuDetailsResult.skuDetailsList ?: emptyList()
                        val skuList = skuDetailsList.map {
                            SkuDetails(
                                description = it.description,
                                sku = it.sku
                            )
                        }

                        billingClientStateCallback?.onBillingSetupFinished(skuList)
                    }
                }

            } else {
                billingClientStateCallback?.onFailure(billingResult.responseCode)
            }
        }

        override fun onBillingServiceDisconnected() {
            billingClientStateCallback = null
        }
    }

    private var billingClientStateCallback: BillingClientStateCallback? = null

    val isReady = billingClient.isReady

    fun startConnection(billingClientStateCallback: BillingClientStateCallback) {
        try {
            this.billingClientStateCallback = billingClientStateCallback

            val connected = BillingClient.ConnectionState.CONNECTED
            val connecting = BillingClient.ConnectionState.CONNECTING

            with(billingClient) {
                if (connectionState.not(connected) and connectionState.not(connecting)) {
                    billingClient.startConnection(billingClientStateListener)
                }
            }
        } catch (e: IllegalStateException) {
            billingClientStateCallback.onFailure(e)
        }
    }

    fun endConnection() {
        billingClient.endConnection()
        billingClientStateCallback = null
    }

    fun querySkuDetails(@MainThread onSkuDetailsResult: (SkuDetailsResult) -> Unit) {
        val builder = SkuDetailsParams.newBuilder().apply {
            setSkusList(skusList).setType(BillingClient.SkuType.INAPP)
        }

        coroutineScope.launch {
            val skuDetailsResult = billingClient.querySkuDetails(builder.build())

            withContext(Dispatchers.Main) {
                onSkuDetailsResult.invoke(skuDetailsResult)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        when {
            consumableSkuList.containsAll(purchase.skus) -> {
                val consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                coroutineScope.launch {
                    val consumeResult = billingClient.consumePurchase(consumeParams)

                    withContext(Dispatchers.Main) {
                        if (consumeResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            // TODO purchase callback 따로 구성할 것. onSuccess
                        }
                    }
                }
            }

            purchase.purchaseState.`is`(Purchase.PurchaseState.PURCHASED) -> {
                if (purchase.isAcknowledged.not()) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)

                    coroutineScope.launch {
                        val billingResult = billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())

                        withContext(Dispatchers.Main) {
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                // TODO purchase callback 따로 구성할 것. onSuccess
                            } else {

                                // TODO fail 도 purchase callback 에서 따로 처리할 것.
                                billingClientStateCallback?.onFailure(billingResult.responseCode)
                            }
                        }
                    }
                }
            }
        }
    }

    suspend fun isPurchased(sku: String): Boolean {
        billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP).purchasesList.let { purchasesList ->
            for (purchase in purchasesList) {
                if (purchase.skus.contains(sku)) {
                    if (purchase.isAcknowledged && purchase.isPurchased) {
                        return true
                    }
                }
            }

            return false
        }
    }

    private val Purchase.isPurchased: Boolean
        get() = purchaseState.`is`(Purchase.PurchaseState.PURCHASED)
}