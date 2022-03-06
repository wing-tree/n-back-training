package com.wing.tree.n.back.training.presentation.view.training

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.Green500
import com.wing.tree.n.back.training.presentation.ui.theme.Red500
import com.wing.tree.n.back.training.presentation.ui.theme.Typography
import com.wing.tree.n.back.training.presentation.ui.theme.verticalPadding
import com.wing.tree.n.back.training.presentation.util.isNull
import com.wing.tree.n.back.training.presentation.util.notNull
import com.wing.tree.n.back.training.presentation.view.core.SebangText
import com.wing.tree.n.back.training.presentation.viewmodel.TrainingViewModel

@ExperimentalFoundationApi
@Composable
internal fun Result(viewModel: TrainingViewModel, onConfirmButtonClick: () -> Unit) {
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ResultContent(viewModel = viewModel, modifier = Modifier.weight(1.0F))

        Surface(elevation = 4.dp) {
            Button(
                onClick = { onConfirmButtonClick() },
                modifier = Modifier
                    .padding(24.dp, 12.dp)
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = CircleShape
            ) {
                SebangText(text = context.getString(R.string.confirm), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
internal fun ResultDialog(onDismissRequest: () -> Unit, viewModel: TrainingViewModel, onButtonClick: () -> Unit) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.fillMaxWidth().verticalPadding(72.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                ResultContent(viewModel = viewModel, Modifier.weight(1.0F))

                Surface(elevation = 4.dp) {
                    Button(
                        onClick = onButtonClick,
                        modifier = Modifier
                            .padding(24.dp, 12.dp)
                            .height(40.dp)
                            .fillMaxWidth(),
                        shape = CircleShape
                    ) {
                        SebangText(text = context.getString(R.string.confirm), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
internal fun ResultContent(viewModel: TrainingViewModel, modifier: Modifier = Modifier) {
    val correctAnswerCount = viewModel.problems.filter { it.isCorrect }.count()
    val solutionNotNullCount = viewModel.problems.filter { it.solution.notNull }.count()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        SebangText(
            text = "$correctAnswerCount/$solutionNotNullCount",
            fontSize = 36.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            modifier = Modifier.weight(1.0F),
            cells = GridCells.Adaptive(minSize = 72.dp)
        ) {
            items(viewModel.problems) { item ->
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val color = when {
                        item.isCorrect -> Green500
                        item.answer.isNull && item.solution.isNull -> Color.Gray
                        else -> Red500
                    }

                    SebangText(
                        text = "${item.number}",
                        color = color,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    when(item.answer) {
                        true -> R.drawable.ic_o_24
                        false -> R.drawable.ic_x_24
                        else -> R.drawable.ic_hypen_24
                    }.also {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = BLANK,
                            colorFilter = ColorFilter.tint(color)
                        )
                    }
                }
            }
        }
    }
}