package com.wing.tree.n.back.training.presentation.view.billing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ApplicationTheme {
                Scaffold {
                    Column {
                        //TopAppbar(
                    }
                }
            }
        }
    }
}