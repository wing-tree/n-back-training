package com.wing.tree.n.back.training.presentation.view.training

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.constant.ONE_SECOND
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationColor
import com.wing.tree.n.back.training.presentation.ui.theme.horizontalPadding
import com.wing.tree.n.back.training.presentation.ui.theme.verticalPadding
import com.wing.tree.n.back.training.presentation.util.`is`
import com.wing.tree.n.back.training.presentation.util.isNull
import com.wing.tree.n.back.training.presentation.util.quarter
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.viewmodel.TrainingViewModel
import kotlinx.coroutines.delay

@Composable
internal fun Training(viewModel: TrainingViewModel, trainingParameter: TrainingParameter?) {
    val enabled by rememberUpdatedState(trainingParameter?.enabled ?: true)
    val visible by rememberUpdatedState(trainingParameter?.visible ?: true)

    val rounds = viewModel.rounds

    var round by rememberSaveable { mutableStateOf(0) }

    if (round.`is`(rounds)) {
        LaunchedEffect(round) {
            viewModel.end()
        }

        return
    } else {
        LaunchedEffect(round) {
            delay(ONE_SECOND.quarter)

            viewModel.setEnabled(round >= viewModel.n)
            viewModel.setVisible(true)

            delay(ONE_SECOND.times(viewModel.speed))

            round += 1
            viewModel.setVisible(false)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SebangText(text = "${round.inc()}/$rounds", fontSize = 36.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0F)
                    .horizontalPadding(24.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 4.dp
            ) {
                val text = if (visible) {
                    "${viewModel.problems[round].number}"
                } else {
                    BLANK
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    SebangText(text = text, fontSize = 64.sp)
                }
            }

            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .horizontalPadding(24.dp)
                .verticalPadding(36.dp, 56.dp)
            ) {
                Button(
                    onClick = {
                        val problem = viewModel.problems[round]

                        if (problem.answer.isNull) {
                            problem.answer = true

                            viewModel.setEnabled(false)

                            if (viewModel.speedMode) {
                                round += 1
                                viewModel.setVisible(false)
                            }
                        }
                    },
                    modifier = Modifier
                        .height(72.dp)
                        .weight(1.0F),
                    enabled = enabled,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = ApplicationColor.Green)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_o_24),
                        contentDescription = BLANK,
                        modifier = Modifier.size(36.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Button(
                    onClick = {
                        val problem = viewModel.problems[round]

                        if (problem.answer.isNull) {
                            problem.answer = false

                            viewModel.setEnabled(false)

                            if (viewModel.speedMode) {
                                round += 1
                                viewModel.setVisible(false)
                            }
                        }
                    },
                    modifier = Modifier
                        .height(72.dp)
                        .weight(1.0F),
                    enabled = enabled,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = ApplicationColor.Red)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_x_24),
                        contentDescription = BLANK,
                        modifier = Modifier.size(36.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        }
    }
}

data class TrainingParameter(
    val enabled: Boolean,
    val visible: Boolean
)