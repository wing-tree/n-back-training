package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.ui.theme.Green500
import com.wing.tree.n.back.training.presentation.ui.theme.Red500
import com.wing.tree.n.back.training.presentation.view.core.SebangText
import com.wing.tree.n.back.training.presentation.view.core.TopAppbar

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
                            TrainingSample(Modifier.fillMaxSize())
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
            Spacer(modifier = Modifier.height(24.dp))

            SebangText(text = "13/20", fontSize = 36.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0F)
                        .padding(24.dp, 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        SebangText(text = "5", fontSize = 64.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp, 36.dp)) {
                    Button(
                        onClick = {
                        },
                        modifier = Modifier
                            .height(48.dp)
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
                            .height(48.dp)
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