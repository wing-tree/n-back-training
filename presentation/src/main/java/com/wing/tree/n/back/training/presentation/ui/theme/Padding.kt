package com.wing.tree.n.back.training.presentation.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal fun Modifier.horizontalPadding(padding: Dp) = padding(padding, 0.dp)
internal fun Modifier.verticalPadding(padding: Dp) = padding(0.dp, padding)

internal fun Modifier.paddingBottom(bottom: Dp) = padding(0.dp, 0.dp, 0.dp, bottom)
internal fun Modifier.paddingEnd(end: Dp) = padding(0.dp, 0.dp, end, 0.dp)
internal fun Modifier.paddingStart(start: Dp) = padding(start, 0.dp, 0.dp, 0.dp)
internal fun Modifier.paddingTop(top: Dp) = padding(0.dp, top, 0.dp, 0.dp)