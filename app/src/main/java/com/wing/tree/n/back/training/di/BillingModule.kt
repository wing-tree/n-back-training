package com.wing.tree.n.back.training.di

import android.content.Context
import com.wing.tree.n.back.training.data.billing.BillingModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object BillingModule {
    @Provides
    @Singleton
    fun providesBillingModule(@ApplicationContext context: Context): BillingModule {
        return BillingModule(context)
    }
}