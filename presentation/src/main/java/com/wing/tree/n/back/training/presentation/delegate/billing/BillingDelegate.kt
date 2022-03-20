package com.wing.tree.n.back.training.presentation.delegate.billing

import android.content.Context

interface BillingDelegate {
    fun startBillingClientConnection(
        context: Context,
        billingCallback: BillingCallback
    )
}