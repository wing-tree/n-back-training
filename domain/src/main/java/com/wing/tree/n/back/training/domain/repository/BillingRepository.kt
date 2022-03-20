package com.wing.tree.n.back.training.domain.repository

import com.wing.tree.n.back.training.domain.model.billing.BillingClientStateCallback
import com.wing.tree.n.back.training.domain.model.billing.SkuDetails

interface BillingRepository {
    fun endConnection()
    fun getSkuDetailsList(onSuccess: (List<SkuDetails>) -> Unit, onFailure: (Exception) -> Unit)
    fun startConnection(billingClientStateCallback: BillingClientStateCallback)
    suspend fun isRemoveAdsPurchased(
        onSuccess: (purchased: Boolean) -> Unit,
        onFailure: (exception: Exception) -> Unit
    )
}