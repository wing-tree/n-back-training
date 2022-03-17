package com.wing.tree.n.back.training.presentation.view.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wing.tree.n.back.training.presentation.ui.theme.paddingEnd
import com.wing.tree.n.back.training.presentation.ui.theme.sebangFamily

@Composable
internal fun SebangText(
    text: String,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        Spacer(modifier = Modifier.height(1.dp))

        Text(
            text = text,
            color = color,
            lineHeight = lineHeight,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontFamily = sebangFamily,
                textAlign = textAlign
            )
        )
    }
}

@Composable
internal fun NumberedSebangText(
    number: Int,
    text: String,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = 20.sp
) {
    Row(modifier = modifier) {
       SebangText(
           text = "$number",
           modifier= Modifier.paddingEnd(8.dp),
           color = color,
           fontSize = fontSize,
           fontWeight = FontWeight.Bold
       )

       SebangText(
           text = text,
           verticalArrangement = verticalArrangement,
           horizontalAlignment = horizontalAlignment,
           color = color,
           fontSize = fontSize,
           fontWeight = fontWeight,
           textAlign = textAlign,
           lineHeight = lineHeight
       )
    }
}