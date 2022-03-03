package com.wing.tree.n.back.training.presentation.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.wing.tree.n.back.training.presentation.BuildConfig
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.view.composable.ButtonText
import com.wing.tree.n.back.training.presentation.view.composable.CancelAlertDialog
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.constant.ONE_SECOND
import com.wing.tree.n.back.training.presentation.constant.PACKAGE_NAME
import com.wing.tree.n.back.training.presentation.model.Menu
import com.wing.tree.n.back.training.presentation.ui.theme.*
import com.wing.tree.n.back.training.presentation.util.*
import com.wing.tree.n.back.training.presentation.view.composable.Header
import com.wing.tree.n.back.training.presentation.view.ranking.RankingActivity
import com.wing.tree.n.back.training.presentation.viewmodel.TrainingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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

                if (state is State.RankingRegistration) {
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

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Header(
                            title = title ?: "${viewModel.n}-Back",
                            modifier = Modifier,
                            navigationIcon = { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = BLANK) },
                            navigationOnClick = {
                                when(state) {
                                    is State.Result -> {
                                        interstitialAd?.show(this@TrainingActivity) ?: finish()
                                    }
                                    is State.RankingRegistration -> {
                                        showCancelAlertDialog = true
                                    }
                                    else -> finish()
                                }
                            },
                            Menu.Text(
                                text = if (viewModel.speedMode) {
                                    getString(R.string.speed_mode)
                                } else {
                                    getString(R.string.normal_mode)
                                }
                            )
                        )

                        Spacer(modifier = Modifier.height(36.dp))

                        if (showCancelAlertDialog) {
                            CancelAlertDialog(
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
                            composable(Route.RANKING_REGISTRATION) {
                                RankingRegistration(
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
                            is State.Ready -> {
                                viewModel.ready()
                            }
                            is State.Training -> navController.navigate(Route.TRAINING) {
                                viewModel.train()

                                launchSingleTop = true
                                popUpTo(Route.READY) { inclusive = true }
                            }
                            is State.Result -> navController.navigate(Route.RESULT) {
                                launchSingleTop = true
                                popUpTo(Route.TRAINING) { inclusive = true }
                            }
                            is State.RankingRegistration -> navController.navigate(Route.RANKING_REGISTRATION) {
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
        const val RANKING_REGISTRATION = "$PACKAGE_NAME.$OBJECT_NAME.RANKING_REGISTRATION"
    }

    sealed class State {
        object Ready : State()
        object Training : State()
        object Result : State()
        object RankingRegistration: State()
    }
}

@Composable
private fun Ready(readyParameter: ReadyParameter?) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = readyParameter?.text ?: BLANK,
            modifier = Modifier
                .align(Alignment.Center)
                .paddingBottom(160.dp),
            style = TextStyle(
                fontSize = 57.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = sebangFamily,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun Training(viewModel: TrainingViewModel, trainingParameter: TrainingParameter?) {
    val enabled by rememberUpdatedState(trainingParameter?.enabled ?: true)
    val visible by rememberUpdatedState(trainingParameter?.visible ?: true)

    val rounds = viewModel.rounds

    var round by rememberSaveable { mutableStateOf(0) }

    if (round.`is`(rounds)) {
        LaunchedEffect(round) {
            viewModel.complete()
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

    Spacer(modifier = Modifier.height(12.dp))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${round.inc()}/$rounds",
            style = TextStyle(
                fontSize = 34.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = sebangFamily,
                textAlign = TextAlign.Center
            )
        )
        
        Spacer(modifier = Modifier.height(36.dp))

        Column {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0F)
                    .padding(24.dp, 0.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 4.dp
            ) {
                val text = if (visible) {
                    "${viewModel.problems[round].number}"
                } else {
                    BLANK
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = text,
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = sebangFamily,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp, 36.dp)
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
}

data class ReadyParameter(
    val text: String
)

data class TrainingParameter(
    val enabled: Boolean,
    val visible: Boolean
)

@ExperimentalFoundationApi
@Composable
private fun Result(viewModel: TrainingViewModel, onButtonClick: () -> Unit) {
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ResultContent(viewModel = viewModel)

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
                style = Typography.button
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun ResultContent(viewModel: TrainingViewModel, modifier: Modifier = Modifier) {
    val correctAnswerCount = viewModel.problems.filter { it.isCorrect }.count()
    val solutionNotNullCount = viewModel.problems.filter { it.solution.notNull }.count()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.padding(24.dp),
            text = "$correctAnswerCount/$solutionNotNullCount",
            style = TextStyle(
                fontSize = 45.sp,
                fontFamily = sebangFamily,
                textAlign = TextAlign.Center
            )
        )

        LazyVerticalGrid(
            modifier = Modifier.weight(1.0F),
            cells = GridCells.Adaptive(minSize = 72.dp)
        ) {
            items(viewModel.problems) { item ->
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
                        color = color,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = sebangFamily,
                            textAlign = TextAlign.Center
                        )
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

@ExperimentalFoundationApi
@Composable
private fun RankingRegistration(
    viewModel: TrainingViewModel,
    onCancelButtonClick: () -> Unit,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val context = LocalContext.current

    @Composable
    fun Option(title: String, value: Any?, modifier: Modifier = Modifier) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(0.dp, 12.dp)) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1.0F),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = sebangFamily,
                        textAlign = TextAlign.Center
                    )
                )

                if (value.notNull) {
                    Text(
                        text = "$value",
                        modifier = Modifier.weight(1.0F),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = sebangFamily,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun ResultDialog(onDismissRequest: () -> Unit, onButtonClick: () -> Unit) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 72.dp),
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
                            Text(
                                text = context.getString(R.string.confirm),
                                style = Typography.button
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CountrySelectionDialog(onDismissRequest: () -> Unit, onClick: (Locale) -> Unit) {
        val items = mutableListOf<Locale>()
        val availableLocales = Locale.getAvailableLocales().filter { it.country.trim().isNotBlank() }
        val displayCountries = mutableListOf<String>()
        val isoCountries = Locale.getISOCountries()

        availableLocales.forEach {
            if (isoCountries.contains(it.country)) {
                if (displayCountries.contains(it.displayCountry).not()) {
                    displayCountries.add(it.displayCountry)
                    items.add(it)
                }
            }
        }

        items.sortBy { it.displayCountry }

        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 72.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(0.dp, 12.dp)) {
                    items(items) {
                        Row(
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth()
                                .clickable { onClick(it) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.padding(24.dp, 0.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = it.flagEmoji)

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = it.displayCountry,
                                    modifier = Modifier.textPadding(),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = sebangFamily,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var locale by remember {
            mutableStateOf(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.resources.configuration.locales[0]
                } else {
                    @Suppress("DEPRECATION")
                    context.resources.configuration.locale
                }
            )
        }

        var name by remember { mutableStateOf(BLANK) }
        var isError by remember { mutableStateOf(false) }

        var showCountrySelectionDialog by remember { mutableStateOf(false) }
        var showResultDialog by remember { mutableStateOf(false) }

        if (showCountrySelectionDialog) {
            CountrySelectionDialog(onDismissRequest = { showCountrySelectionDialog = false }) {
                showCountrySelectionDialog = false
                locale = it
            }
        }

        if (showResultDialog) {
            ResultDialog(onDismissRequest = { showResultDialog = false }) {
                showResultDialog = false
            }
        }

        LazyColumn(modifier = Modifier.weight(1.0F)) {
            item {
                Column(
                    modifier = Modifier.padding(24.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Option(
                        title = "${viewModel.n}-Back",
                        value = null,
                        modifier = Modifier.padding(48.dp, 0.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Option(
                        title = context.getString(R.string.elapsed_time),
                        value = "${viewModel.elapsedTime / 1000.0}"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Option(
                        title = context.getString(R.string.rounds),
                        value = viewModel.rounds
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Option(
                        title = context.getString(R.string.speed),
                        value = viewModel.speed
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.padding(24.dp, 0.dp)) {
                        Button(
                            onClick = { context.startActivity(Intent(context, RankingActivity::class.java)) },
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1.0F),
                            shape = CircleShape
                        ) {
                            ButtonText(text = context.getString(R.string.ranking))
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        Button(
                            onClick = { showResultDialog = true },
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1.0F),
                            shape = CircleShape
                        ) {
                            ButtonText(text = context.getString(R.string.result))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(context.getString(R.string.name)) },
                        shape = RoundedCornerShape(12.dp),
                        isError = isError
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                            .padding(24.dp, 0.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable {
                                    showCountrySelectionDialog = true
                                }
                                .padding(24.dp, 0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = locale.flagEmoji)

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = locale.displayCountry,
                                    modifier = Modifier.textPadding(),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = sebangFamily,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Image(Icons.Rounded.ArrowDropDown, BLANK)
                            }
                        }
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.Bottom) {
            Surface(elevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onCancelButtonClick,
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1.0F),
                        shape = CircleShape
                    ) {
                        ButtonText(text = context.getString(R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                viewModel.registerForRanking(
                                    name,
                                    locale.country,
                                    onSuccess = onSuccess
                                ) {
                                    onFailure(it)
                                }
                            } else {
                                isError = true
                            }
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1.0F),
                        shape = CircleShape
                    ) {
                        ButtonText(text = context.getString(R.string.register))
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.congratulation))
        val progress by animateLottieCompositionAsState(composition)

        if (progress < 1.0F) {
            LottieAnimation(
                composition,
                progress
            )
        }
    }
}