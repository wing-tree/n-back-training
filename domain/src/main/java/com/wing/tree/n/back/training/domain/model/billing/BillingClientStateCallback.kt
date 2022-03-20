package com.wing.tree.n.back.training.domain.model.billing

import androidx.annotation.MainThread

interface BillingClientStateCallback {
    fun onBillingSetupFinished(jsonSkuDetailsList: List<String>)
    @MainThread
    fun onFailure(exception: Exception)
    @MainThread
    fun onFailure(responseCode: Int)
}