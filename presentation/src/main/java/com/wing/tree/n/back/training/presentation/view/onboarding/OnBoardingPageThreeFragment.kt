package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.model.Problem
import com.wing.tree.n.back.training.presentation.ui.theme.*
import com.wing.tree.n.back.training.presentation.ui.theme.horizontalPadding
import com.wing.tree.n.back.training.presentation.util.isNull
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar

class OnBoardingPageThreeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ApplicationTheme {
                    Scaffold {
                        Column {
                            TopAppbar(title = getString(R.string.result_screen))

                            Column(
                                modifier = Modifier.verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(4.dp))

                                ResultSample()

                                Spacer(modifier = Modifier.height(24.dp))

                                Column {
                                    SebangText(
                                        text = "1. ${getString(R.string.result_screen1)}",
                                        modifier = Modifier.horizontalPadding(24.dp),
                                        textAlign = TextAlign.Start
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    SebangText(
                                        text = "2. ${getString(R.string.result_screen2)}",
                                        modifier = Modifier.horizontalPadding(24.dp),
                                        textAlign = TextAlign.Start
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    SebangText(
                                        text = "3. ${getString(R.string.result_screen3)}",
                                        modifier = Modifier.horizontalPadding(24.dp),
                                        textAlign = TextAlign.Start
                                    )
                                }

                                Spacer(modifier = Modifier.height(112.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

private val sampleProblems = listOf(
    listOf(
        Problem(
            solution = null,
            number = 5,
            answer = null
        ),
        Problem(
            solution = null,
            number = 4,
            answer = null
        ),
        Problem(
            solution = null,
            number = 1,
            answer = null
        ),
        Problem(
            solution = false,
            number = 3,
            answer = false
        ),
        Problem(
            solution = true,
            number = 4,
            answer = true
        ),
        Problem(
            solution = true,
            number = 1,
            answer = true
        )
    ),
    listOf(
        Problem(
            solution = true,
            number = 3,
            answer = true
        ),
        Problem(
            solution = false,
            number = 5,
            answer = false
        ),
        Problem(
            solution = false,
            number = 5,
            answer = false
        ),
        Problem(
            solution = true,
            number = 3,
            answer = true
        ),
        Problem(
            solution = false,
            number = 2,
            answer = false
        ),
        Problem(
            solution = false,
            number = 3,
            answer = true
        )
    ),
    listOf(
        Problem(
            solution = false,
            number = 1,
            answer = false
        ),
        Problem(
            solution = true,
            number = 2,
            answer = true
        ),
        Problem(
            solution = true,
            number = 3,
            answer = true
        ),
        Problem(
            solution = false,
            number = 4,
            answer = false
        ),
        Problem(
            solution = false,
            number = 4,
            answer = false
        ),
        Problem(
            solution = true,
            number = 3,
            answer = true
        )
    ),
    listOf(
        Problem(
            solution = true,
            number = 4,
            answer = false
        ),
        Problem(
            solution = false,
            number = 5,
            answer = false
        )
    ),
)

@Composable
private fun ResultSample(modifier: Modifier = Modifier) {
    @Composable
    fun ResultItem(item: Problem, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.padding(9.dp),
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(3.dp))

            when(item.answer) {
                true -> R.drawable.ic_o_24
                false -> R.drawable.ic_x_24
                else -> R.drawable.ic_hypen_24
            }.also {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = BLANK,
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(color)
                )
            }
        }
    }

    Surface(
        modifier = Modifier
            .wrapContentSize()
            .horizontalPadding(12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Column(modifier = modifier.horizontalPadding(9.dp).verticalPadding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            SebangText(
                text = "15/17",
                fontSize = 27.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Column {
                sampleProblems.forEach {
                    Row { it.forEach { ResultItem(item = it, modifier = Modifier.horizontalPadding(3.dp)) } }
                }
            }
        }
    }
}