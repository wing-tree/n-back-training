package com.wing.tree.n.back.training.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wing.tree.n.back.training.presentation.model.Menu

@Composable
internal fun Header(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: @Composable () -> Unit,
    navigationOnClick: () -> Unit,
    vararg menu: Menu
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)) {
            IconButton(
                onClick = navigationOnClick,
                content = navigationIcon
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                menu.forEach {
                    if (it is Menu.Item) {
                        IconButton(onClick = { it.onClick?.invoke() }) {
                            Icon(painterResource(id = it.icon), it.title)
                        }
                    }
                }
            }
        }

        Text(
            text = title,
            style = TextStyle(
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}