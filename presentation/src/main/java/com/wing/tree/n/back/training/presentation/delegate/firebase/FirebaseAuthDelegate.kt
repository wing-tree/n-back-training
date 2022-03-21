package com.wing.tree.n.back.training.presentation.delegate.firebase

import android.app.Activity

interface FirebaseAuthDelegate {
    fun signInAnonymously(
        activity: Activity,
        onSuccess: (uid: String) -> Unit,
        onFailure: (Exception) -> Unit
    )
}