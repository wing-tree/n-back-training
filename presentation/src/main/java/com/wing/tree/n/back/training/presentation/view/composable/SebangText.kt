package com.wing.tree.n.back.training.presentation.view.composable

import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wing.tree.n.back.training.presentation.ui.theme.sebangFamily

@Composable
internal fun SebangText(text: String, modifier: Modifier = Modifier, fontSize: TextUnit = 14.sp, fontWeight: FontWeight = FontWeight.Normal) {
    Text(
        text = text,
        modifier = modifier.paddingFromBaseline(14.dp, 4.dp),
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = sebangFamily,
            textAlign = TextAlign.Center
        )
    )
}