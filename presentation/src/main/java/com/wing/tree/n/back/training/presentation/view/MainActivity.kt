package com.wing.tree.n.back.training.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.constant.Extra
import com.wing.tree.n.back.training.presentation.constant.N
import com.wing.tree.n.back.training.presentation.constant.Rounds
import com.wing.tree.n.back.training.presentation.constant.Speed
import com.wing.tree.n.back.training.presentation.model.Menu
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.util.*
import com.wing.tree.n.back.training.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ApplicationTheme {
                val coroutineScope = rememberCoroutineScope()
                val scaffoldState = rememberScaffoldState()

                val option = viewModel.option

                val menuList = listOf(
                    Menu.Item(R.drawable.ic_round_history_24, getString(R.string.record)) {
                        startActivity(Intent(this, RecordActivity::class.java))

                        coroutineScope.launch {
                            with(scaffoldState.drawerState) {
                                if (isOpen) {
                                    close()
                                }
                            }
                        }
                    },
                    Menu.Divider,
                    Menu.Item(R.drawable.ic_round_review_24, getString(R.string.write_review)) {
                        Review.launchReviewFlow(this)
                    },
                    Menu.Item(R.drawable.ic_round_share_24, getString(R.string.share_the_app)) {
                        shareApplication(this)
                    },
                    Menu.Item(R.drawable.ic_round_info_24, getString(R.string.version), versionName)
                )

                BackHandler(scaffoldState.drawerState.isOpen) {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }

                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        Drawer(menuList)
                    },
                    topBar = {
                        TopAppBar(
                            title = { Text(getString(R.string.app_name)) },
                            navigationIcon = {
                                Icon(
                                    Icons.Rounded.Menu,
                                    null,
                                    modifier = Modifier.clickable(onClick = {
                                        coroutineScope.launch {
                                            with(scaffoldState.drawerState) {
                                                if (isClosed) {
                                                    open()
                                                } else {
                                                    close()
                                                }
                                            }
                                        }
                                    })
                                )
                            }
                        )
                    }
                ) {
                    val scrollState = rememberScrollState()

                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                           Button(onClick = {

                           }) {

                           }

                            Button(onClick = {

                            }) {

                            }
                        }

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.0F)
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                        ) {
                            val modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()

                            Option(
                                modifier = modifier,
                                title = getString(R.string.n_back),
                                initialValue = option.n.float,
                                valueRange = N.ValueRange,
                                steps = N.STEPS
                            ) { option.n = it.int }

                            Spacer(modifier = Modifier.height(8.dp))

                            Option(
                                modifier = modifier,
                                title = getString(R.string.rounds),
                                initialValue = option.rounds.float,
                                valueRange = Rounds.ValueRange,
                                steps = Rounds.STEPS
                            ) { option.rounds = it.int }

                            Spacer(modifier = Modifier.height(8.dp))

                            Option(
                                modifier = modifier,
                                title = getString(R.string.speed),
                                initialValue = option.speed.float,
                                valueRange = Speed.ValueRange,
                                steps = Speed.STEPS
                            ) { option.speed = it.int }
                        }

                        Button(
                            onClick = {
                                with(Intent(applicationContext, TrainingActivity::class.java)) {
                                    putExtra(Extra.OPTION, option)

                                    startActivity(this)
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            shape = CircleShape
                        ) {
                            Text(
                                text = "${getString(R.string.start).uppercase()}!",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }

                        AdView()
                    }
                }
            }
        }
    }
}

@Composable
private fun Drawer(menuList: List<Menu>) {
    menuList.forEach {
        when(it) {
            is Menu.Divider -> Divider()
            is Menu.Item -> Menu(item = it)
        }
    }
}

@Composable
private fun Menu(modifier: Modifier = Modifier, item: Menu.Item) {
    val height = if (item.caption.isBlank()) {
        56.dp
    } else {
        72.dp
    }

    Row(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clickable(item.onClick.notNull) { item.onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        
        Image(painter = painterResource(item.icon), contentDescription = null)
        
        Spacer(modifier = Modifier.width(32.dp))

        Column {
            Text(
                text = item.title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            )
            
            if (item.caption.isNotBlank()) {
                Text(
                    text = item.caption,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )
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
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "${valueFinished.int}",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Slider(
                value = value,
                onValueChange = { value = it },
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = {
                    valueFinished = value
                    onValueChangeFinished.invoke(valueFinished)
                }
            )
        }
    }
}

@Composable
fun AdView(modifier: Modifier = Modifier) {
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