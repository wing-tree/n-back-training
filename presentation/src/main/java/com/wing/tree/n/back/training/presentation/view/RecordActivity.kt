package com.wing.tree.n.back.training.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wing.tree.n.back.training.domain.model.SortBy
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.constant.PACKAGE_NAME
import com.wing.tree.n.back.training.presentation.model.Menu
import com.wing.tree.n.back.training.presentation.model.Record
import com.wing.tree.n.back.training.presentation.ui.theme.*
import com.wing.tree.n.back.training.presentation.util.isNull
import com.wing.tree.n.back.training.presentation.util.notNull
import com.wing.tree.n.back.training.presentation.view.shared.ConfirmAlertDialog
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar
import com.wing.tree.n.back.training.presentation.viewmodel.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RecordActivity : ComponentActivity() {
    private val viewModel by viewModels<RecordViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            var showDialog by remember { mutableStateOf(false) }
            var sortBy by remember { mutableStateOf(viewModel.sortBy) }

            ApplicationTheme {
                Scaffold {
                    Column {
                        TopAppbar(
                            title = getString(R.string.records),
                            modifier = Modifier,
                            navigationIcon = {
                                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = BLANK)
                            },
                            navigationOnClick = {
                                onBackPressed()
                            },
                            footer = {
                            },
                            elevation = 4.dp,
                            Menu.Item(
                                icon = R.drawable.ic_round_sort_24,
                                title = getString(R.string.sort),
                                onClick = {
                                    showDialog = true
                                }
                            )
                        )

                        if (showDialog) {
                            SortDialog(sortBy) {
                                viewModel.putSortBy(it.value)

                                sortBy = it
                                showDialog = false
                            }
                        }

                        NavHost(navController = navController, startDestination = Route.RECORD_LIST) {
                            composable(route = Route.RECORD_LIST) {
                                RecordList(navController, viewModel, sortBy)
                            }
                            composable(route = Route.RESULT) { navBackStackEntry ->
                                navBackStackEntry.arguments?.getParcelable<Record>(Key.RECORD)?.let {
                                    Result(it) { onBackPressed() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    object Key {
        private const val OBJECT_NAME = "Key"

        const val RECORD = "$PACKAGE_NAME.$OBJECT_NAME.RECORD"
    }

    object Route {
        private const val OBJECT_NAME = "Route"

        const val RECORD_LIST = "$PACKAGE_NAME.$OBJECT_NAME.RECORD_LIST"
        const val RESULT = "$PACKAGE_NAME.$OBJECT_NAME.RESULT"
    }
}

@ExperimentalFoundationApi
@Composable
fun RecordList(navController: NavController, viewModel: RecordViewModel, sortBy: SortBy) {
    val context = LocalContext.current
    val records: List<Record>? by viewModel.records.observeAsState()
    var selectedRecord by remember { mutableStateOf<Record?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val items = when(sortBy) {
        SortBy.Newest -> records?.sortedByDescending { it.timestamp }
        SortBy.Oldest -> records?.sortedBy { it.timestamp }
    } ?: return

    if (showDialog) {
        selectedRecord?.let {
            ConfirmAlertDialog(
                onDismissRequest = { showDialog = false },
                title = context.getString(R.string.delete_record),
                text = context.getString(R.string.delete_the_record),
                confirmButtonText = context.getString(R.string.delete),
                onConfirmButtonClick = {
                    viewModel.delete(it)
                    showDialog = false
                }
            ) {
                showDialog = false
            }
        }
    }

    LazyColumn {
        item { Spacer(modifier = Modifier.height(12.dp)) }
        
        items(items, key = { it }) { record ->
            RecordItem(
                Modifier
                    .horizontalPadding(16.dp)
                    .paddingBottom(12.dp)
                    .animateItemPlacement(),
                record,
                onClick = {
                    navController.navigate(route = RecordActivity.Route.RESULT, Bundle().apply {
                        putParcelable(RecordActivity.Key.RECORD, it)
                    })
                },
                onDeleteIconClick = {
                    selectedRecord = it
                    showDialog = true
                }
            )
        }
    }
}

@Composable
fun RecordItem(modifier: Modifier, record: Record, onClick: (Record) -> Unit, onDeleteIconClick: (Record) -> Unit) {
    val context = LocalContext.current

    fun getString(@StringRes resId: Int) = context.getString(resId)

    @Composable
    fun Option(labelText: String, value: Any?, modifier: Modifier = Modifier) {
        Card(shape = RoundedCornerShape(8.dp)) {
            Row(modifier = modifier.verticalPadding(8.dp)) {
                SebangText(text = labelText, modifier = Modifier.weight(1.0F))

                value?.let {
                    SebangText(text = "$it", modifier = Modifier.weight(1.0F))
                }
            }
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(record) }
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingTop(4.dp),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.0F),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(24.dp))

                        SebangText(
                            text = "${record.n}-Back",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        SebangText(
                            text = record.result,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = { onDeleteIconClick.invoke(record) }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = BLANK,
                            tint = ApplicationColor.Icon
                        )
                    }
                }

                Column(modifier = Modifier.horizontalPadding(16.dp)) {
                    val timestamp = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(record.timestamp)

                    Spacer(modifier = Modifier.height(4.dp))

                    Option(labelText = getString(R.string.elapsed_time), value = String.format("%.3f", record.elapsedTime / 1_000_000_000.0))

                    Spacer(modifier = Modifier.height(8.dp))

                    Option(labelText = getString(R.string.rounds), value = record.rounds)

                    Spacer(modifier = Modifier.height(8.dp))

                    Option(labelText = getString(R.string.speed), value = record.speed)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        SebangText(text = timestamp, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
internal fun Result(record: Record, onButtonClick: () -> Unit) {
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(4.dp))

        SebangText(
            modifier = Modifier.verticalPadding(24.dp),
            text = record.result,
            fontSize = 36.sp,
            textAlign = TextAlign.Center
        )

        Divider()

        LazyVerticalGrid(
            modifier = Modifier.weight(1.0F),
            contentPadding = PaddingValues(12.dp),
            cells = GridCells.Adaptive(minSize = 72.dp)
        ) {
            items(record.problems) { item ->
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val color = when {
                        item.correct -> ApplicationColor.Green
                        item.answer.isNull && item.solution.isNull -> ApplicationColor.Gray
                        else -> ApplicationColor.Red
                    }

                    SebangText(
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

        Surface(
            elevation = 4.dp
        ) {
            Button(
                onClick = { onButtonClick() },
                modifier = Modifier
                    .padding(16.dp)
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = CircleShape
            ) {
                SebangText(
                    text = context.getString(R.string.confirm),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SortDialog(value: SortBy, onDismissRequest: (SortBy) -> Unit) {
    val context = LocalContext.current
    var sortBy by remember { mutableStateOf(value) }

    Dialog(onDismissRequest = {
        onDismissRequest.invoke(sortBy)
    }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp, 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { sortBy = SortBy.Newest },
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SebangText(
                        text = context.getString(R.string.newest),
                        modifier = Modifier
                            .weight(1.0F)
                            .horizontalPadding(12.dp),
                        horizontalAlignment = Alignment.Start,
                        textAlign = TextAlign.Start
                    )

                    RadioButton(
                        selected = sortBy.value == SortBy.Newest.value,
                        onClick = { sortBy = SortBy.Newest },
                        enabled = true
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { sortBy = SortBy.Oldest },
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SebangText(
                        text = context.getString(R.string.oldest),
                        modifier = Modifier
                            .weight(1.0F)
                            .horizontalPadding(12.dp),
                        horizontalAlignment = Alignment.Start,
                        textAlign = TextAlign.Start
                    )

                    RadioButton(
                        selected = sortBy.value == SortBy.Oldest.value,
                        onClick = { sortBy = SortBy.Oldest },
                        enabled = true
                    )
                }
            }
        }
    }
}

private fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val navDeepLinkRequest = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(route).toUri())
        .build()

    graph.matchDeepLink(navDeepLinkRequest)?.let {
        val destination = it.destination
        val id = destination.id

        navigate(id, args, navOptions, navigatorExtras)
    } ?: navigate(route, navOptions, navigatorExtras)
}