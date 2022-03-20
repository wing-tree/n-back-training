package com.wing.tree.n.back.training.data.repository

import com.wing.tree.n.back.training.data.datasource.network.billing.BillingDataSource
import com.wing.tree.n.back.training.domain.model.billing.BillingClientStateCallback
import com.wing.tree.n.back.training.domain.model.billing.Sku
import com.wing.tree.n.back.training.domain.model.billing.SkuDetails
import com.wing.tree.n.back.training.domain.repository.BillingRepository
import javax.inject.Inject

class BillingRepositoryImpl @Inject constructor(private val billingDataSource: BillingDataSource): BillingRepository {
    override fun endConnection() {
        billingDataSource.endConnection()
    }

    override fun getSkuDetailsList(
        onSuccess: (List<SkuDetails>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        billingDataSource.getSkuDetailsList(onSuccess, onFailure)
    }

    override suspend fun isRemoveAdsPurchased(
        onSuccess: (purchased: Boolean) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        billingDataSource.isPurchased(
            sku = Sku.REMOVE_ADS,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override fun startConnection(billingClientStateCallback: BillingClientStateCallback) {
        billingDataSource.startConnection(billingClientStateCallback)
    }
}