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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.wing.tree.n.back.training.presentation.BuildConfig
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.*
import com.wing.tree.n.back.training.presentation.model.Menu
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.ui.theme.sebangFamily
import com.wing.tree.n.back.training.presentation.util.*
import com.wing.tree.n.back.training.presentation.view.composable.TopAppbar
import com.wing.tree.n.back.training.presentation.view.RecordActivity
import com.wing.tree.n.back.training.presentation.view.TrainingActivity
import com.wing.tree.n.back.training.presentation.view.onboarding.OnBoardingActivity
import com.wing.tree.n.back.training.presentation.view.ranking.RankingActivity
import com.wing.tree.n.back.training.presentation.ui.theme.textPadding
import com.wing.tree.n.back.training.presentation.view.composable.ButtonText
import com.wing.tree.n.back.training.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    private val menu by lazy {
        listOf(
            Menu.Item(R.drawable.ic_round_history_24, getString(R.string.record)) {
                startActivity(Intent(this, RecordActivity::class.java))
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
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 4.dp,
                            shape = RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp)
                        ) {
                            Column {
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
                                                getString(R.string.how_to_play) to {
                                                    startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
                                                },
                                                getString(R.string.ranking) to {
                                                    startActivity(Intent(this@MainActivity, RankingActivity::class.java))
                                                }
                                            )

                                            Spacer(modifier = Modifier.height(24.dp))
                                        }
                                    },
                                    Menu.Switch(
                                        option.speedMode,
                                        getString(R.string.speed_mode),
                                        getString(R.string.speed_mode)
                                    ) {
                                        option.speedMode = it
                                    }
                                )
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.0F)
                        ) {
                            item {
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp, 12.dp)
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

                                    Spacer(modifier = Modifier.height(12.dp))

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
                                        putExtra(Extra.BACK, it)
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
    Column(modifier = Modifier.padding(12.dp, 0.dp)) {
        menu.forEach {
            when(it) {
                is Menu.Divider -> Divider(modifier = Modifier.padding(16.dp, 0.dp))
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

            Image(painter = painterResource(item.icon), contentDescription = null)

            Spacer(modifier = Modifier.width(12.dp))

            Row {
                Text(
                    text = item.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = sebangFamily,
                        textAlign = TextAlign.Center
                    )
                )

                if (item.subtext.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.subtext,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = sebangFamily,
                                textAlign = TextAlign.Center
                            )
                        )

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
                ButtonText(text = pair.first)
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
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp, 0.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    modifier = Modifier
                        .textPadding()
                        .weight(1.0F),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = sebangFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "${valueFinished.int}",
                    modifier = Modifier
                        .textPadding()
                        .weight(1.0F),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = sebangFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
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
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onClick(it) },
                modifier = Modifier
                    .padding(24.dp, 0.dp)
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = CircleShape
            ) {
                ButtonText(text = "$it-Back")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun AdView(modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .height(50.dp)
        .fillMaxWidth()) {
        if (LocalInspectionMode.current.not()) {
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
                    }
                }
            )
        }
    }
}