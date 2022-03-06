package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.*
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.constant.From
import com.wing.tree.n.back.training.presentation.constant.ONE_HUNDRED
import com.wing.tree.n.back.training.presentation.constant.Offset
import com.wing.tree.n.back.training.presentation.constant.Until
import com.wing.tree.n.back.training.presentation.model.Problem
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.ui.theme.Green500
import com.wing.tree.n.back.training.presentation.ui.theme.Red500
import com.wing.tree.n.back.training.presentation.util.isNull
import com.wing.tree.n.back.training.presentation.util.quarter
import com.wing.tree.n.back.training.presentation.view.core.SebangText
import com.wing.tree.n.back.training.presentation.view.core.TopAppbar
import kotlin.random.Random

@ExperimentalFoundationApi
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
                            ResultSample()
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun ResultSample(modifier: Modifier = Modifier) {
    val n = 3
    val rounds = 20

    val problems: List<com.wing.tree.n.back.training.domain.model.Problem> = run {
        var intArray = IntArray(rounds)
        var previousTrueCount = 0

        repeat(ONE_HUNDRED.quarter) {
            val intRange = IntRange(1, 5)

            var trueCount = 0

            with(IntArray(rounds) { intRange.random() }) {
                repeat(rounds) {
                    if (it >= n) {
                        if (get(it - n) == get(it)) {
                            ++trueCount
                        }
                    }
                }

                if (previousTrueCount < trueCount) {
                    intArray = this.clone()
                    previousTrueCount = trueCount
                }
            }
        }

        val solutionArray by lazy {
            val array = arrayOfNulls<Boolean?>(rounds)

            repeat(rounds) {
                array[it] = if (it < n) {
                    null
                } else {
                    intArray[it - n] == intArray[it]
                }
            }

            array
        }

        intArray.zip(solutionArray) { value, solution ->
            object : com.wing.tree.n.back.training.domain.model.Problem() {
                override val solution: Boolean?
                    get() = solution
                override val number: Int
                    get() = value
                override var answer: Boolean? = null
            }
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        SebangText(
            text = "15/17",
            fontSize = 36.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            modifier = Modifier.weight(1.0F),
            cells = GridCells.Adaptive(minSize = 72.dp)
        ) {
            items(problems) { item ->
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