package com.wing.tree.n.back.training.presentation.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.wing.tree.n.back.training.presentation.BuildConfig
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.*
import com.wing.tree.n.back.training.presentation.model.Menu
import com.wing.tree.n.back.training.presentation.timber.TimberSetup
import com.wing.tree.n.back.training.presentation.timber.TimberSetupImpl
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.ui.theme.horizontalPadding
import com.wing.tree.n.back.training.presentation.ui.theme.paddingBottom
import com.wing.tree.n.back.training.presentation.ui.theme.paddingTop
import com.wing.tree.n.back.training.presentation.util.*
import com.wing.tree.n.back.training.presentation.view.RecordActivity
import com.wing.tree.n.back.training.presentation.view.onboarding.OnBoardingActivity
import com.wing.tree.n.back.training.presentation.view.ranking.RankingActivity
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar
import com.wing.tree.n.back.training.presentation.view.training.TrainingActivity
import com.wing.tree.n.back.training.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity(), TimberSetup by TimberSetupImpl() {
    private val viewModel by viewModels<MainViewModel>()

    private val menu by lazy {
        listOf(
            Menu.Item(R.drawable.ic_driving_guidelines_96px, getString(R.string.how_to_play)) {
                startActivity<OnBoardingActivity>()
            },
            Menu.Item(R.drawable.ic_prize_96px, getString(R.string.ranking)) {
                startActivity<RankingActivity>()
            },
            Menu.Item(R.drawable.ic_round_history_24, getString(R.string.record)) {
                startActivity<RecordActivity>()
            },
            Menu.Divider,
            Menu.Item(R.drawable.ic_round_rate_review_24, getString(R.string.write_review)) {
                Review.launchReviewFlow(this)
            },
            Menu.Item(R.drawable.ic_round_share_24, getString(R.string.share_the_app)) {
                shareApplication(this)
            },
            Menu.Item(R.drawable.ic_round_info_24, getString(R.string.version), versionName) {
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTimber()

        if (viewModel.isFirstTime) {
            startActivity<OnBoardingActivity>()
            viewModel.updateIsFirstTimeToFalse()
        }

        setContent {
            ApplicationTheme {
                val coroutineScope = rememberCoroutineScope()
                val scaffoldState = rememberScaffoldState()

                val option = viewModel.option

                BackHandler(scaffoldState.drawerState.isOpen) {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }

                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerContent = { Drawer(menu) },
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TopAppbar(
                            title = getString(R.string.app_name),
                            modifier = Modifier,
                            navigationIcon = {
                                Icon(imageVector = Icons.Rounded.Menu, contentDescription = BLANK)
                            },
                            navigationOnClick = {
                                coroutineScope.launch {
                                    with(scaffoldState.drawerState) {
                                        if (isClosed) {
                                            open()
                                        } else {
                                            close()
                                        }
                                    }
                                }
                            },
                            footer = {
                                Column {
                                    HorizontalButtonGroup(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp, 0.dp),
                                        getString(R.string.how_to_play) to { startActivity<OnBoardingActivity>() },
                                        getString(R.string.ranking) to { startActivity<RankingActivity>() }
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            },
                            elevation = 4.dp,
                            Menu.Switch(
                                option.speedMode,
                                getString(R.string.speed_mode),
                                getString(R.string.speed_mode)
                            ) {
                                option.speedMode = it
                            }
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.0F)
                        ) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalPadding(24.dp)
                                        .paddingBottom(8.dp)
                                        .paddingTop(24.dp)
                                ) {
                                    val modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()

                                    Option(
                                        modifier = modifier,
                                        title = getString(R.string.rounds),
                                        initialValue = option.rounds.float,
                                        valueRange = Rounds.ValueRange,
                                        steps = Rounds.STEPS
                                    ) {
                                        if (it < Rounds.ValueRange.start) {
                                            option.rounds = Rounds.DEFAULT
                                        } else {
                                            option.rounds = it.int
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Option(
                                        modifier = modifier,
                                        title = getString(R.string.speed),
                                        initialValue = option.speed.float,
                                        valueRange = Speed.ValueRange,
                                        steps = Speed.STEPS
                                    ) {
                                        if (it < Speed.ValueRange.start) {
                                            option.speed = Speed.DEFAULT
                                        } else {
                                            option.speed = it.int
                                        }
                                    }
                                }
                            }

                            item {
                                NBackButtonGroup(modifier = Modifier.fillMaxWidth()) {
                                    with(Intent(applicationContext, TrainingActivity::class.java)) {
                                        putExtra(Extra.N, it)
                                        putExtra(Extra.OPTION, option)

                                        startActivity(this)
                                    }
                                }
                            }
                        }

                        AdView()
                    }
                }
            }
        }
    }
}

@Composable
private fun Drawer(menu: List<Menu>) {
    Column(modifier = Modifier.horizontalPadding(12.dp)) {
        menu.forEach {
            when(it) {
                is Menu.Divider -> Divider(modifier = Modifier.horizontalPadding(16.dp))
                is Menu.Item -> Menu(item = it)
                else -> {
                }
            }
        }
    }
}

@Composable
private fun Menu(modifier: Modifier = Modifier, item: Menu.Item) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .clip(CircleShape)
            .clickable(item.onClick.notNull) { item.onClick() }
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(item.icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(item.tint)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Row {
                SebangText(text = item.title, fontWeight = FontWeight.Bold)

                if (item.subtext.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SebangText(text = item.subtext, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.width(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun HorizontalButtonGroup(modifier: Modifier = Modifier, vararg pairs: Pair<String, () -> Unit>) {
    val count = pairs.count()
    
    Row(modifier = modifier) {
        pairs.forEachIndexed { index, pair ->
            Button(
                onClick = { pair.second.invoke() },
                modifier = Modifier
                    .height(40.dp)
                    .weight(1.0F),
                shape = CircleShape
            ) {
                SebangText(text = pair.first, fontWeight = FontWeight.Bold)
            }
            
            if (index.not(count.dec())) {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Composable
private fun Option(
    modifier: Modifier = Modifier,
    title: String,
    initialValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChangeFinished: (value: Float) -> Unit
) {
    var value by remember { mutableStateOf(initialValue) }
    var valueFinished by remember { mutableStateOf(initialValue) }

    Card(
        modifier = modifier,
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp, 0.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                SebangText(
                    text = title,
                    modifier = Modifier.weight(1.0F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(12.dp))

                SebangText(
                    text = "${valueFinished.int}",
                    modifier = Modifier.weight(1.0F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Slider(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.padding(4.dp, 0.dp),
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = {
                    valueFinished = value
                    onValueChangeFinished.invoke(valueFinished)
                }
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun NBackButtonGroup(modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    Column(modifier = modifier) {
        N.IntRange.forEach {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onClick(it) },
                modifier = Modifier
                    .padding(24.dp, 0.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
                shape = CircleShape
            ) {
                SebangText(
                    text = "$it-Back",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AdView(modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(true) }

    if (LocalInspectionMode.current.not()) {
        if (visible) {
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            ) {
                AndroidView(
                    modifier = modifier.fillMaxWidth(),
                    factory = { context ->
                        AdView(context).apply {
                            adSize = AdSize.BANNER
                            adUnitId = context.getString(
                                if (BuildConfig.DEBUG) {
                                    R.string.sample_banner_ad_unit_id
                                } else {
                                    R.string.banner_ad_unit_id
                                }
                            )

                            loadAd(AdRequest.Builder().build())

                            adListener = object : AdListener() {
                                override fun onAdFailedToLoad(adError: LoadAdError) {
                                    super.onAdFailedToLoad(adError)
                                    visible = false
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}