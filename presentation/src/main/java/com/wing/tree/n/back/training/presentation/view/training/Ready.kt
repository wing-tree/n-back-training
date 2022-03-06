package com.wing.tree.n.back.training.presentation.view.training

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.paddingBottom
import com.wing.tree.n.back.training.presentation.view.core.SebangText

@Composable
internal fun Ready(readyParameter: ReadyParameter?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SebangText(
            text = readyParameter?.text ?: BLANK,
            modifier = Modifier.paddingBottom(36.dp),
            fontSize = 57.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

data class ReadyParameter(
    val text: String
)