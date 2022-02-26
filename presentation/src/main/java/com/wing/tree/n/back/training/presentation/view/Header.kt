package com.wing.tree.n.back.training.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wing.tree.n.back.training.presentation.model.Menu
import com.wing.tree.n.back.training.presentation.ui.theme.sebangFamily

@Composable
internal fun Header(
    title: String,
    modifier: Modifier = Modifier,
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
                    when(it) {
                        is Menu.Item -> {
                            IconButton(onClick = { it.onClick() }) {
                                Icon(painterResource(id = it.icon), it.title)
                            }
                        }
                        is Menu.Switch -> {
                            var checked by remember { mutableStateOf(it.checked) }
                            var text by remember {
                                mutableStateOf(
                                    if (it.checked) {
                                        it.textOn
                                    } else {
                                        it.textOff
                                    }
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = text,
                                    modifier = Modifier.padding(0.dp, 1.dp, 0.dp, 0.dp),
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(fontFamily = sebangFamily)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Switch(
                                    checked = checked, onCheckedChange = { isChecked ->
                                        checked = isChecked

                                        with(it) {
                                            onCheckedChanged(isChecked)

                                            text = if (checked) {
                                                textOn
                                            } else {
                                                textOff
                                            }
                                        }
                                    }
                                )
                            }

                        }
                        else -> {
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = sebangFamily,
                textAlign = TextAlign.Center
            )
        )
    }
}