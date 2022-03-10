package com.wing.tree.n.back.training.presentation.view.shared

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wing.tree.n.back.training.presentation.R

@Composable
internal fun ConfirmAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { SebangText(text = title) },
        text = { SebangText(text = text) },
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