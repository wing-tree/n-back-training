package com.wing.tree.n.back.training.data.datasource.network.billing

import com.wing.tree.n.back.training.data.billing.BillingModule
import com.wing.tree.n.back.training.domain.model.billing.BillingClientStateCallback
import com.wing.tree.n.back.training.domain.model.billing.SkuDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BillingDataSourceImpl @Inject constructor(private val billingModule: BillingModule) : BillingDataSource {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun endConnection() {
        billingModule.endConnection()
    }

    override fun getSkuDetailsList(onSuccess: (List<SkuDetails>) -> Unit, onFailure: (Exception) -> Unit) {
        if (billingModule.isReady) {
            billingModule.querySkuDetails { skuDetailsResult ->
                val skuDetailsList = skuDetailsResult.skuDetailsList?.map {
                    SkuDetails(
                        description = it.description,
                        sku = it.sku
                    )
                }

                onSuccess(skuDetailsList ?: emptyList())
            }
        } else {
            billingModule.startConnection(object : BillingClientStateCallback {
                override fun onBillingSetupFinished(skuDetailsList: List<SkuDetails>) {
                    onSuccess(skuDetailsList)
                }

                override fun onFailure(exception: Exception) {
                    onFailure(exception)
                }

                override fun onFailure(responseCode: Int) {
                    onFailure(IllegalStateException("responseCode :$responseCode"))
                }
            })
        }
    }

    override fun startConnection(billingClientStateCallback: BillingClientStateCallback) {
        billingModule.startConnection(billingClientStateCallback)
    }

    override suspend fun isPurchased(
        sku: String,
        onSuccess: (purchased: Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (billingModule.isReady) {
            val purchased = billingModule.isPurchased(sku)

            withContext(Dispatchers.Main) {
                onSuccess(purchased)
            }
        } else {
            billingModule.startConnection(object : BillingClientStateCallback {
                override fun onBillingSetupFinished(skuDetailsList: List<SkuDetails>) {
                    coroutineScope.launch {
                        val purchased = billingModule.isPurchased(sku)

                        withContext(Dispatchers.Main) {
                            onSuccess(purchased)
                        }
                    }
                }

                override fun onFailure(exception: Exception) {
                    onFailure(exception)
                }

                override fun onFailure(responseCode: Int) {
                    onFailure(IllegalStateException("responseCode :$responseCode"))
                }
            })
        }
    }
}