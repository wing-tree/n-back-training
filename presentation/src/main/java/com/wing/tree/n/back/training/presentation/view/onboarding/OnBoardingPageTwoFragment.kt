package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.*
import com.wing.tree.n.back.training.presentation.ui.theme.horizontalPadding
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar

class OnBoardingPageTwoFragment : Fragment() {
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
                            TopAppbar(title = getString(R.string.play_screen))

                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Spacer(modifier = Modifier.height(4.dp))

                                TrainingSample(modifier = Modifier.horizontalPadding(24.dp))

                                Spacer(modifier = Modifier.height(24.dp))

                                SebangText(
                                    text = "1. ${getString(R.string.play_screen1)}",
                                    modifier = Modifier.horizontalPadding(24.dp),
                                    textAlign = TextAlign.Start
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                SebangText(
                                    text = "2. ${getString(R.string.play_screen2)}",
                                    modifier = Modifier.horizontalPadding(24.dp),
                                    textAlign = TextAlign.Start
                                )
                                
                                Spacer(modifier = Modifier.height(112.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrainingSample(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(18.dp))

            SebangText(text = "13/20", fontSize = 24.sp)

            Spacer(modifier = Modifier.height(18.dp))

            Column {
                Surface(
                    modifier = Modifier.horizontalPadding(24.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalPadding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        SebangText(text = "5", fontSize = 48.sp)
                    }
                }

                Spacer(modifier = Modifier.height(9.dp))

                Row(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp, 27.dp)) {
                    Button(
                        onClick = {
                        },
                        modifier = Modifier
                            .height(36.dp)
                            .weight(1.0F),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Green500)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_o_24),
                            contentDescription = BLANK,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Button(
                        onClick = {
                        },
                        modifier = Modifier
                            .height(36.dp)
                            .weight(1.0F),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Red500)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_x_24),
                            contentDescription = BLANK,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
            }
        }
    }
}