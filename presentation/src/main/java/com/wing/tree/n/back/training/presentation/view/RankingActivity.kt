package com.wing.tree.n.back.training.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.viewmodel.RankingViewModel

class RankingActivity : ComponentActivity() {
    private val viewModel by viewModels<RankingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ApplicationTheme {
                Scaffold {

                }
            }
        }
    }
}