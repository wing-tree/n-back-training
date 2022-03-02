package com.wing.tree.n.back.training.presentation.view.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wing.tree.n.back.training.presentation.ui.theme.Typography
import com.wing.tree.n.back.training.presentation.ui.theme.textPadding

@Composable
internal fun ButtonText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.textPadding(),
        style = Typography.button
    )
}