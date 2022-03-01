package com.wing.tree.n.back.training.presentation.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.ui.theme.sebangFamily
import com.wing.tree.n.back.training.presentation.view.textPadding

@Composable
internal fun CancelAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                modifier = Modifier.textPadding(),
                style = TextStyle(fontFamily = sebangFamily)
            )
        },
        text = {
            Text(
                text = text,
                modifier = Modifier.textPadding(),
                style = TextStyle(fontFamily = sebangFamily)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text(context.getString(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonClick) {
                Text(context.getString(R.string.dismiss))
            }
        },
        shape = RoundedCornerShape(12.dp),
    )
}