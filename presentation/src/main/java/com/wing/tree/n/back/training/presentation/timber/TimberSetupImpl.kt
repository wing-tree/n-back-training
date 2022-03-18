package com.wing.tree.n.back.training.presentation.timber

import timber.log.Timber

class TimberSetupImpl : TimberSetup {
    override fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}