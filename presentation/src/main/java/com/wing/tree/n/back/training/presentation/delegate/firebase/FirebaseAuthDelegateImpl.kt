package com.wing.tree.n.back.training.presentation.delegate.firebase

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthDelegateImpl : FirebaseAuthDelegate {
    override fun signInAnonymously(
        activity: Activity,
        onSuccess: (uid: String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        currentUser?.let {
            onSuccess(it.uid)
        } ?: run {
            firebaseAuth
                .signInAnonymously()
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        firebaseAuth.currentUser?.uid?.let { uid ->
                            onSuccess(uid)
                        } ?: run {
                            onFailure(NullPointerException("firebaseAuth.currentUser?.uid :${firebaseAuth.currentUser?.uid}"))
                        }
                    } else {
                        onFailure(task.exception ?: NullPointerException("task.exception :${task.exception}"))
                    }
                }
        }
    }
}