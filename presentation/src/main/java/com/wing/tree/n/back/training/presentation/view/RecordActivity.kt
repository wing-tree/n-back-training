package com.wing.tree.n.back.training.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wing.tree.n.back.training.presentation.viewmodel.RecordViewModel
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.PACKAGE_NAME
import com.wing.tree.n.back.training.presentation.model.Record
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.util.notNull
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

            ApplicationTheme {
                Scaffold {
                    NavHost(navController = navController, startDestination = Route.RECORD_LIST) {
                        composable(route = Route.RECORD_LIST) {
                            RecordList(viewModel, navController)
                        }
                        composable(route = Route.DETAIL) { navBackStackEntry ->
                            navBackStackEntry.arguments?.getParcelable<Record>(Key.RECORD)?.let {
                                Detail(it)
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
        const val DETAIL = "$PACKAGE_NAME.$OBJECT_NAME.DETAIL"
    }
}

@Composable
fun RecordList(viewModel: RecordViewModel, navController: NavController) {
    val records: List<Record>? by viewModel.recordList.observeAsState()
    val items = records ?: emptyList()

    LazyColumn {
        items(items) { record ->
            RecordItem(Modifier.padding(16.dp, 8.dp), record) {
                navController.navigate(route = RecordActivity.Route.DETAIL, Bundle().apply {
                    putParcelable(RecordActivity.Key.RECORD, it)
                })
            }
        }
    }
}

@Composable
fun RecordItem(modifier: Modifier, record: Record, onClick: (Record) -> Unit) {
    val context = LocalContext.current

    Card(
        modifier
            .fillMaxWidth()
            .clickable { onClick(record) }
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            Column {
                val rounds = "${context.getString(R.string.rounds)} ${record.rounds}"
                val speed = "${context.getString(R.string.speed)} ${record.speed}"
                val time = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(record.timestamp)

                Text(text = rounds)
                Text(text = speed)
                Text(text = time)
            }

            val correctAnswerCount = record.problemList.map { it.isCorrect }.count()
            val solutionNotNullCount = record.problemList.map { it.solution.notNull }.count()

            Text(text = "$correctAnswerCount/$solutionNotNullCount")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun Detail(record: Record) {
    val correctAnswerCount = record.problemList.map { it.isCorrect }.count()
    val solutionNotNullCount = record.problemList.map { it.solution.notNull }.count()

    Column {
        Text("$correctAnswerCount/$solutionNotNullCount")
        LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 72.dp)) {
            items(record.problemList) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${item.number}")
                    Text(text = "${item.solution}")
                    Text(text = "${item.answer}")
                }
            }
        }
    }
}

fun NavController.navigate(
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