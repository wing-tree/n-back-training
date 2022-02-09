package com.wing.tree.n.back.training.presentation.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.google.android.play.core.review.ReviewManagerFactory
import com.wing.tree.n.back.training.presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Review {
    fun launchReviewFlow(activity: Activity) {
        val reviewManager = ReviewManagerFactory.create(activity)
        val task = reviewManager.requestReviewFlow()

        task.addOnCompleteListener {
            if (it.isSuccessful) {
                val reviewInfo = it.result

                reviewInfo.describeContents()
                reviewManager.launchReviewFlow(activity, reviewInfo).apply {
                    addOnCompleteListener {
                        CoroutineScope(Dispatchers.Main).launch {
                            activity.getString(R.string.thank_you_for_your_valuable_review).run {
                                Toast.makeText(activity, this, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            } else {
                goToPlayStore(activity)
            }
        }
    }

    private fun goToPlayStore(context: Context) {
        try {
            context.startActivity(
                Intent (
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${context.packageName}"))
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent (
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                )
            )
        }
    }
}