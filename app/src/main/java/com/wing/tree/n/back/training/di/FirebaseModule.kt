package com.wing.tree.n.back.training.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wing.tree.n.back.training.data.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object FirebaseModule {
    @Provides
    @Singleton
    fun providesFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }
}