package com.wing.tree.n.back.training.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wing.tree.n.back.training.presentation.constant.Back
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme

class RankingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ApplicationTheme {
                Scaffold {
                    BackList()
                }
            }
        }
    }
}

@Composable
private fun BackList(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Back.IntRange.forEach {
            BackItem(back = it)
        }
    }
}

@Composable
private fun BackItem(modifier: Modifier = Modifier, back: Int) {
    Card(modifier = modifier.fillMaxWidth()) {
        Text(text = "$back-Back")
    }
}