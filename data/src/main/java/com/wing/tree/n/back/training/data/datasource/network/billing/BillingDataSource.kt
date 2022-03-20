package com.wing.tree.n.back.training.data.datasource.network.billing

import com.wing.tree.n.back.training.domain.model.billing.BillingClientStateCallback
import com.wing.tree.n.back.training.domain.model.billing.SkuDetails

interface BillingDataSource {
    fun endConnection()
    fun getSkuDetailsList(onSuccess: (List<SkuDetails>) -> Unit, onFailure: (Exception) -> Unit)
    fun startConnection(billingClientStateCallback: BillingClientStateCallback)
    suspend fun isPurchased(sku: String, onSuccess: (purchased: Boolean) -> Unit, onFailure: (Exception) -> Unit)
}