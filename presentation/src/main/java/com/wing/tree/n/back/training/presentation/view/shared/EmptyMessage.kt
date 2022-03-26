package com.wing.tree.n.back.training.presentation.view.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationColor

@Composable
fun EmptyMessage(message: String, modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_empty_state_168_140),
                contentDescription = BLANK
            )

            Spacer(modifier = Modifier.height(16.dp))

            SebangText(
                text = message,
                color = ApplicationColor.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
fun EmptyMessage(@StringRes message: Int, modifier: Modifier) {
    EmptyMessage(message = LocalContext.current.getString(message), modifier = modifier)
}