package com.wing.tree.n.back.training.presentation.view.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.paddingEnd
import com.wing.tree.n.back.training.presentation.ui.theme.sebangFamily
import java.util.regex.Pattern

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
internal fun SebangText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified,
    wordToColorHashMap: HashMap<String, Color> = hashMapOf()
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        Spacer(modifier = Modifier.height(1.dp))

        val words = wordToColorHashMap.keys.map { it }
        val regex = with(words.joinToString(BLANK) { "|$it" }) {
            "((?=:$this)|(?<=:$this))"
        }

        Text(
            text = buildAnnotatedString {
                text.split(Pattern.compile(regex)).forEach {
                    wordToColorHashMap[it]?.let { color ->
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold, color = color)
                        ) {
                            append(it)
                        }
                    } ?: run {
                        append(it)
                    }
                }
            },
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
    lineHeight: TextUnit = 20.sp,
    wordToColorHashMap: HashMap<String, Color> = hashMapOf()
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
           text = AnnotatedString.Builder(text).toAnnotatedString(),
           verticalArrangement = verticalArrangement,
           horizontalAlignment = horizontalAlignment,
           color = color,
           fontSize = fontSize,
           fontWeight = fontWeight,
           textAlign = textAlign,
           lineHeight = lineHeight,
           wordToColorHashMap = wordToColorHashMap
       )
    }
}