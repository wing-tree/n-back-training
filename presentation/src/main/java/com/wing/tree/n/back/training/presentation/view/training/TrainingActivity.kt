package com.wing.tree.n.back.training.presentation.view.training

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.wing.tree.n.back.training.presentation.model.Menu
import com.wing.tree.n.back.training.presentation.ui.theme.*
import com.wing.tree.n.back.training.presentation.util.*
import com.wing.tree.n.back.training.presentation.view.shared.ConfirmAlertDialog
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar
import com.wing.tree.n.back.training.presentation.viewmodel.TrainingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
            var showCancelAlertDialog by remember { mutableStateOf(false) }

            BackHandler(true) {
                if (state is State.Result) {
                    interstitialAd?.show(this) ?: finish()
                    return@BackHandler
                }

                if (state is State.Ranking) {
                    showCancelAlertDialog = true
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
                    val readyParameter by viewModel.readyParameter.observeAsState()
                    val trainingParameter by viewModel.trainingParameter.observeAsState()
                    val title by viewModel.title.observeAsState()

                    var elevation by remember { mutableStateOf(0.dp) }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        TopAppbar(
                            title = title ?: "${viewModel.n}-Back",
                            modifier = Modifier,
                            navigationIcon = { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = BLANK) },
                            navigationOnClick = {
                                when(state) {
                                    is State.Result -> {
                                        interstitialAd?.show(this@TrainingActivity) ?: finish()
                                    }
                                    is State.Ranking -> {
                                        showCancelAlertDialog = true
                                    }
                                    else -> finish()
                                }
                            },
                            footer = {
                                if (state is State.Result) {
                                    ResultTitle(viewModel = viewModel, modifier = Modifier.paddingBottom(36.dp))
                                }
                            },
                            elevation = elevation,
                            Menu.Text(
                                text = if (viewModel.speedMode) {
                                    getString(R.string.speed_mode)
                                } else {
                                    getString(R.string.normal_mode)
                                }
                            )
                        )

                        if (showCancelAlertDialog) {
                            ConfirmAlertDialog(
                                onDismissRequest = { showCancelAlertDialog = false },
                                title = getString(R.string.cancel_ranking_registration_title),
                                text = getString(R.string.cancel_ranking_registration_text),
                                onConfirmButtonClick = { finish() }) {
                                showCancelAlertDialog = false
                            }
                        }

                        NavHost(navController = navController, startDestination = Route.READY) {
                            composable(Route.READY) { Ready(readyParameter) }
                            composable(Route.TRAINING) { Training(viewModel, trainingParameter) }
                            composable(Route.RESULT) {
                               Result(viewModel) {
                                    interstitialAd?.show(this@TrainingActivity) ?: finish()
                                }
                            }
                            composable(Route.RANKING) {
                                Ranking(
                                    viewModel = viewModel,
                                    onCancelButtonClick = { showCancelAlertDialog = true },
                                    onSuccess = {
                                        Toast.makeText(
                                            this@TrainingActivity,
                                            getString(R.string.ranking_registered),
                                            Toast.LENGTH_LONG
                                        ).show()

                                        finish()
                                    },
                                    onFailure = {
                                        Toast.makeText(
                                            this@TrainingActivity,
                                            getString(R.string.ranking_registration_failed),
                                            Toast.LENGTH_LONG
                                        ).show()

                                        finish()
                                    }
                                )
                            }
                        }

                        when(state) {
                            is State.Ready -> viewModel.ready()
                            is State.Training -> navController.navigate(Route.TRAINING) {
                                viewModel.start()

                                launchSingleTop = true
                                popUpTo(Route.READY) { inclusive = true }
                            }
                            is State.Result -> navController.navigate(Route.RESULT) {
                                elevation = 4.dp
                                launchSingleTop = true
                                popUpTo(Route.TRAINING) { inclusive = true }
                            }
                            is State.Ranking -> navController.navigate(Route.RANKING) {
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
        const val RANKING = "$PACKAGE_NAME.$OBJECT_NAME.RANKING"
    }

    sealed class State {
        object Ready : State()
        object Training : State()
        object Result : State()
        object Ranking: State()
    }
}