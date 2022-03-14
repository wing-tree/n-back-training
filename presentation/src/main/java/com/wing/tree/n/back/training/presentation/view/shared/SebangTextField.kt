package com.wing.tree.n.back.training.presentation.view.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.*
import com.wing.tree.n.back.training.presentation.util.ifElse

@Composable
internal fun SebangTextFiled(
    onValueChange: (String) -> Unit,
    hint: String = BLANK,
    hintColor: Color = Color.Gray,
    error: String = BLANK,
    errorColor: Color = Red500,
    isError: Boolean,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    var text by rememberSaveable { mutableStateOf(BLANK) }
    var hasFocus by rememberSaveable { mutableStateOf(false) }

    val borderColor = isError.ifElse(errorColor, hasFocus.ifElse(Primary, Color.Gray))
    val paddingTop = 3.5.plus(14.0.minus(fontSize.value).times(0.5)).dp

    Column {
        Surface(
            modifier = Modifier
                .height(48.dp)
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = CircleShape
                ),
            elevation = 4.dp,
            shape = CircleShape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp, 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                    if (text.isBlank()) {
                        SebangText(
                            text = hint,
                            fontSize = fontSize,
                            color = hintColor
                        )
                    }
                    
                    BasicTextField(
                        value = text,
                        onValueChange = {
                            text = it
                            onValueChange(it)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .paddingTop(paddingTop)
                            .onFocusChanged { focusState ->
                                hasFocus = focusState.hasFocus
                            },
                        textStyle = TextStyle(
                            fontSize = fontSize,
                            fontWeight = fontWeight,
                            fontFamily = sebangFamily,
                            textAlign = textAlign
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(Primary)
                    )
                }
            }
        }
        
        if (isError && error.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            SebangText(
                text = error,
                modifier = Modifier.paddingStart(24.dp),
                color = errorColor
            )
        }
    }
}