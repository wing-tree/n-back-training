package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.wing.tree.n.back.training.domain.util.not
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationColor
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.ui.theme.horizontalPadding
import com.wing.tree.n.back.training.presentation.view.shared.NumberedSebangText
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar

class OnBoardingPageOneFragment : Fragment() {
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
                            TopAppbar(title = getString(R.string.how_to_play))
                            
                            Spacer(modifier = Modifier.height(24.dp))

                            LazyColumn {
                                item {
                                    Column(modifier = Modifier.horizontalPadding(24.dp)) {
                                        NumberedSebangText(
                                            number = 1,
                                            text = getString(R.string.how_to_play1),
                                            textAlign = TextAlign.Start
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        Table()

                                        Spacer(modifier = Modifier.height(24.dp))

                                        NumberedSebangText(
                                            number = 2,
                                            text = getString(R.string.how_to_play2),
                                            textAlign = TextAlign.Start
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        NumberedSebangText(
                                            number = 3,
                                            text = getString(R.string.how_to_play3),
                                            textAlign = TextAlign.Start
                                        )
                                        
                                        Spacer(modifier = Modifier.height(112.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Table(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val answers = listOf(
        TableItem.Answer(null),
        TableItem.Answer(null),
        TableItem.Answer(false),
        TableItem.Answer(true),
        TableItem.Answer(false),
    )

    val numbers = listOf(
        TableItem.Number(2),
        TableItem.Number(5),
        TableItem.Number(3),
        TableItem.Number(5),
        TableItem.Number(1),
    )

    val rounds = listOf(
        TableItem.Round(1),
        TableItem.Round(2),
        TableItem.Round(3),
        TableItem.Round(4),
        TableItem.Round(5),
    )

    fun getString(@StringRes resId: Int) = context.getString(resId)

    @Composable
    fun TableColumn(label: String, items: List<TableItem>, modifier: Modifier = Modifier) {
        val size = items.size

        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            SebangText(
                text = label,
                modifier = Modifier.height(32.dp),
                fontWeight = FontWeight.Bold,
                verticalArrangement = Arrangement.Center
            )

            Spacer(modifier = Modifier.height(4.dp))
            Divider()

            items.forEachIndexed { index, item ->
                when(item) {
                    is TableItem.Answer -> {
                        when(item.value) {
                            true -> R.drawable.ic_o_24 to ApplicationColor.Green
                            false -> R.drawable.ic_x_24 to ApplicationColor.Red
                            else -> R.drawable.ic_hypen_24 to ApplicationColor.Gray
                        }.also { (id, color) ->
                            Box(
                                modifier = Modifier.height(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = id),
                                    contentDescription = BLANK,
                                    modifier = modifier.size(16.dp),
                                    colorFilter = ColorFilter.tint(color)
                                )
                            }
                        }
                    }
                    is TableItem.Number -> SebangText(
                        text = "${item.value}",
                        modifier = Modifier.height(32.dp),
                        verticalArrangement = Arrangement.Center
                    )
                    is TableItem.Round -> SebangText(
                        text = "${item.value}",
                        modifier = Modifier.height(32.dp),
                        verticalArrangement = Arrangement.Center
                    )
                }

                if (index.not(size.dec())) {
                    Divider()
                }
            }
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Row(modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(16.dp, 12.dp)) {
            TableColumn(label = getString(R.string.round), items = rounds, modifier = Modifier.weight(1.0F))

            Divider(modifier = Modifier
                .width(1.dp)
                .fillMaxHeight())

            TableColumn(label = getString(R.string.number), items = numbers, modifier = Modifier.weight(1.0F))

            Divider(modifier = Modifier
                .width(1.dp)
                .fillMaxHeight())

            TableColumn(label = getString(R.string.answer), items = answers, modifier = Modifier.weight(1.0F))
        }
    }
}

sealed class TableItem {
    data class Answer(val value: Boolean?): TableItem()
    data class Number(val value: Int): TableItem()
    data class Round(val value: Int): TableItem()
}
