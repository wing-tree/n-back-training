package com.wing.tree.n.back.training.presentation.view.training

import android.content.Intent
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.*
import com.wing.tree.n.back.training.presentation.ui.theme.horizontalPadding
import com.wing.tree.n.back.training.presentation.ui.theme.verticalPadding
import com.wing.tree.n.back.training.presentation.util.flagEmoji
import com.wing.tree.n.back.training.presentation.util.notNull
import com.wing.tree.n.back.training.presentation.view.ranking.RankingActivity
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.view.shared.SebangTextFiled
import com.wing.tree.n.back.training.presentation.viewmodel.TrainingViewModel
import java.util.*

@ExperimentalFoundationApi
@Composable
internal fun Ranking(
    viewModel: TrainingViewModel,
    onCancelButtonClick: () -> Unit,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val context = LocalContext.current

    fun getString(@StringRes resId: Int)  = context.getString(resId)

    @Composable
    fun Option(title: String, value: Any?, modifier: Modifier = Modifier) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            elevation = 4.dp
        ) {
            Row(modifier = Modifier.verticalPadding(12.dp)) {
                SebangText(
                    text = title,
                    modifier = Modifier.weight(1.0F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                if (value.notNull) {
                    SebangText(
                        text = "$value",
                        modifier = Modifier.weight(1.0F),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                ResultDialog(onDismissRequest = { showResultDialog = false }, viewModel = viewModel) {
                    showResultDialog = false
                }
            }

            LazyColumn(modifier = Modifier.weight(1.0F)) {
                item {
                    Column(
                        modifier = Modifier.horizontalPadding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SebangText(
                            text = "${viewModel.n}-Back",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Option(
                            title = context.getString(R.string.elapsed_time),
                            value = String.format("%.3f", viewModel.elapsedTime / 1_000_000_000.0)
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

                        Row(modifier = Modifier.horizontalPadding(24.dp)) {
                            Button(
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            RankingActivity::class.java
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .height(40.dp)
                                    .weight(1.0F),
                                shape = CircleShape
                            ) {
                                SebangText(text = getString(R.string.ranking), fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.width(24.dp))

                            Button(
                                onClick = { showResultDialog = true },
                                modifier = Modifier
                                    .height(40.dp)
                                    .weight(1.0F),
                                shape = CircleShape
                            ) {
                                SebangText(text = getString(R.string.result), fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Label(label = getString(R.string.name))

                        SebangTextFiled(
                            onValueChange = {
                                if (isError) {
                                    isError = it.isBlank()
                                }

                                name = it
                            },
                            hint = getString(R.string.enter_your_name),
                            error = getString(R.string.enter_your_name),
                            isError = isError,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Label(label = getString(R.string.country))

                        Card(
                            modifier = Modifier
                                .height(48.dp)
                                .fillMaxWidth(),
                            shape = CircleShape,
                            elevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .clickable {
                                        showCountrySelectionDialog = true
                                    }
                                    .horizontalPadding(24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    SebangText(
                                        text = locale.flagEmoji,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    SebangText(
                                        text = locale.displayCountry,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Image(
                                        imageVector = Icons.Rounded.ArrowDropDown,
                                        contentDescription = BLANK,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(56.dp))
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
                            SebangText(text = getString(R.string.cancel), fontWeight = FontWeight.Bold)
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
                            SebangText(text = getString(R.string.register), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.congratulations))
            val progress by animateLottieCompositionAsState(composition)

            if (progress < 1.0F) {
                LottieAnimation(
                    composition,
                    progress
                )
            }
        }
    }
}

@Composable
private fun CountrySelectionDialog(onDismissRequest: () -> Unit, onClick: (Locale) -> Unit) {
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
                .verticalPadding(72.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(12.dp)) {
                items(items) {
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                            .clickable { onClick(it) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.horizontalPadding(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = it.flagEmoji)

                            Spacer(modifier = Modifier.width(12.dp))

                            SebangText(text = it.displayCountry, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Label(label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .paddingBottom(8.dp)
            .paddingStart(24.dp)
    ) {
        SebangText(
            text = label,
            color = Primary,
            fontWeight = FontWeight.Bold
        )
    }
}