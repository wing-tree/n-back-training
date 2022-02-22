package com.wing.tree.n.back.training.presentation.view

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.wing.tree.n.back.training.presentation.BuildConfig
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.constant.ONE_SECOND
import com.wing.tree.n.back.training.presentation.constant.PACKAGE_NAME
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.ui.theme.Green500
import com.wing.tree.n.back.training.presentation.ui.theme.Red500
import com.wing.tree.n.back.training.presentation.util.*
import com.wing.tree.n.back.training.presentation.viewmodel.TrainingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class TrainingActivity : ComponentActivity() {
    private val viewModel by viewModels<TrainingViewModel>()

    private var interstitialAd: InterstitialAd? = null
    private var onBackPressed = false

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupInterstitialAd()

        setContent {
            val navController = rememberNavController()
            val state by viewModel.state.observeAsState()

            BackHandler(true) {
                if (state is State.Result) {
                    interstitialAd?.show(this) ?: finish()

                    return@BackHandler
                }

                if (onBackPressed) {
                    finish()

                    return@BackHandler
                }

                this.onBackPressed = true

                Toast.makeText(this, getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT).show()
                Handler(mainLooper).postDelayed({ onBackPressed = false }, ONE_SECOND.twice)
            }

            ApplicationTheme {
                Scaffold {
                    val countDown by viewModel.countDown.observeAsState()

                    val round by viewModel.round.observeAsState()
                    val isVisible by viewModel.isVisible.observeAsState()
                    val enabled by viewModel.enabled.observeAsState()

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Header(back = viewModel.back) {
                            if (state is State.Result) {
                                interstitialAd?.show(this@TrainingActivity) ?: finish()
                            } else {
                                finish()
                            }
                        }

                        NavHost(navController = navController, startDestination = Route.READY) {
                            composable(Route.READY) { Ready(countDown) }
                            composable(Route.TRAINING) { Training(viewModel, isVisible ?: true) }
                            composable(Route.RESULT) {
                                Detail(viewModel) {
                                    interstitialAd?.show(this@TrainingActivity) ?: finish()
                                }
                            }
                        }

                        when(state) {
                            is State.Ready -> {
                                viewModel.ready()
                            }
                            is State.Training -> navController.navigate(Route.TRAINING) {
                                viewModel.progress()

                                launchSingleTop = true
                                popUpTo(Route.READY) { inclusive = true }
                            }
                            is State.Result -> navController.navigate(Route.RESULT) {
                                launchSingleTop = true
                                popUpTo(Route.TRAINING) { inclusive = true }
                            }
                            else -> throw IllegalStateException("state :$state")
                        }
                    }
                }
            }
        }
    }

    private fun setupInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        val adUnitId = getString(
            if (BuildConfig.DEBUG) {
                R.string.sample_interstitial_ad_unit_id
            } else {
                R.string.interstitial_ad_unit_id
            }
        )

        InterstitialAd.load(this, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                this@TrainingActivity.interstitialAd = interstitialAd
                this@TrainingActivity.interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()

                        if (isFinishing.not()) {
                            finish()
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        super.onAdFailedToShowFullScreenContent(adError)

                        if (isFinishing.not()) {
                            finish()
                        }
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()

                        this@TrainingActivity.interstitialAd = null

                        if (isFinishing.not()) {
                            finish()
                        }
                    }
                }
            }
        })
    }

    private object Route {
        private const val OBJECT_NAME = "Route"

        const val READY = "$PACKAGE_NAME.$OBJECT_NAME.READY"
        const val TRAINING = "$PACKAGE_NAME.$OBJECT_NAME.TRAINING"
        const val RESULT = "$PACKAGE_NAME.$OBJECT_NAME.RESULT"
    }

    sealed class State {
        object Ready : State()
        object Training : State()
        object Result : State()
    }
}

@Composable
private fun Header(modifier: Modifier = Modifier, back: Int, navigationOnClick: () -> Unit) {
   Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = BLANK,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false),
                        onClick = { navigationOnClick.invoke() }
                    )
                    .padding(12.dp)
            )
        }

        Text(
            text = "$back-Back",
            style = TextStyle(
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun Ready(countDown: Int?) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        countDown?.let {
            val text = if (it < 1) {
                "${context.getString(R.string.start).uppercase()}!"
            } else {
                "$it"
            }

            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 57.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Training(viewModel: TrainingViewModel, isVisibleNewVal: Boolean) {
    val context = LocalContext.current
    val isVisible by rememberUpdatedState(isVisibleNewVal)
    val rounds = viewModel.rounds

    var round by rememberSaveable { mutableStateOf(0) }

    if (round >= rounds) {
        LaunchedEffect(round) {
            viewModel.complete()
        }

        return
    } else {
        LaunchedEffect(round) {
            delay(ONE_SECOND.quarter)

            viewModel.setIsVisible(true)

            delay(ONE_SECOND.times(viewModel.speed))

            round += 1
            viewModel.setIsVisible(false)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${round.inc()}/$rounds",
            modifier = Modifier.height(48.dp),
            fontSize = 24.sp
        )

        val unit = if (viewModel.speed.`is`(1)) {
            context.getString(R.string.second)
        } else {
            context.getString(R.string.seconds)
        }

        Text(text = "${viewModel.speed} $unit")

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Gray, RoundedCornerShape(12.dp))
        ) {
            val text = if (isVisible) {
                "${viewModel.problemList[round].number}"
            } else {
                BLANK
            }

            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 57.sp,
                textAlign = TextAlign.Center
            )
        }

        val enabled = round >= viewModel.back && isVisible

        Row(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(24.dp)
        ) {
            Button(
                onClick = {
                    val problem = viewModel.problemList[round]

                    if (problem.answer.isNull) {
                        problem.answer = true
                        round += 1
                        viewModel.setIsVisible(false)
                    }
                },
                modifier = Modifier
                    .height(48.dp)
                    .weight(1.0F),
                enabled = enabled,
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
                    val problem = viewModel.problemList[round]

                    if (problem.answer.isNull) {
                        problem.answer = false
                        round += 1
                        viewModel.setIsVisible(false)
                    }
                },
                modifier = Modifier
                    .height(48.dp)
                    .weight(1.0F),
                enabled = enabled,
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

@ExperimentalFoundationApi
@Composable
fun Detail(viewModel: TrainingViewModel, onButtonClick: () -> Unit) {
    val context = LocalContext.current
    val correctAnswerCount = viewModel.problemList.filter { it.isCorrect }.count()
    val solutionNotNullCount = viewModel.problemList.filter { it.solution.notNull }.count()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "$correctAnswerCount/$solutionNotNullCount",
            fontSize = 45.sp,
            textAlign = TextAlign.Center
        )

        LazyVerticalGrid(
            modifier = Modifier.weight(1.0F),
            cells = GridCells.Adaptive(minSize = 72.dp)
        ) {
            items(viewModel.problemList) { item ->
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val color = when {
                        item.isCorrect -> Green500
                        item.answer.isNull && item.solution.isNull -> Color.Gray
                        else -> Red500
                    }

                    Text(
                        text = "${item.number}",
                        fontSize = 24.sp,
                        color = color,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
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

        Button(
            onClick = { onButtonClick() },
            modifier = Modifier
                .padding(16.dp)
                .height(40.dp)
                .fillMaxWidth(),
            shape = CircleShape
        ) {
            Text(
                text = context.getString(R.string.confirm).uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Companion.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}