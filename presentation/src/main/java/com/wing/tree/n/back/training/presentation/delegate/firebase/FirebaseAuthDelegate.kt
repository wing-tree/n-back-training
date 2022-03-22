package com.wing.tree.n.back.training.presentation.delegate.firebase

interface FirebaseAuthDelegate {
    fun signInAnonymously(
        onSuccess: (uid: String) -> Unit,
        onFailure: (Exception) -> Unit
    )
}