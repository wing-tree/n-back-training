package com.wing.tree.n.back.training.presentation.delegate.billing

interface BillingClientStateCallbacks {
    fun onBillingSetupFinished() = Unit
    fun onFailure(debugMessage: String, responseCode: Int) = Unit
}