package com.wing.tree.n.back.training.presentation.view.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.wing.tree.n.back.training.presentation.ui.theme.Typography
import com.wing.tree.n.back.training.presentation.ui.theme.textPadding

@Composable
internal fun ButtonText(text: String, modifier: Modifier = Modifier) {
    SebangText(text = text, fontWeight = FontWeight.Bold, modifier = modifier)
}