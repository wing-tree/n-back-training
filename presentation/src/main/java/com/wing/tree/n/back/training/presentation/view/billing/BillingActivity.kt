package com.wing.tree.n.back.training.presentation.view.billing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.SkuDetails
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar
import com.wing.tree.n.back.training.presentation.viewmodel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillingActivity : ComponentActivity() {
    private val viewModel by viewModels<BillingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ApplicationTheme {
                Scaffold {
                    Column {
                        TopAppbar(
                            title = getString(R.string.in_app_billing),
                            modifier = Modifier,
                            navigationIcon = {
                                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = BLANK)
                            },
                            navigationOnClick = {
                                onBackPressed()
                            },
                            elevation = 4.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun SkuDetailsList(viewModel: BillingViewModel, modifier: Modifier = Modifier) {
    val skuDetailsList by viewModel.skuDetailsList.observeAsState()

    LazyColumn(modifier = modifier) {
        items(skuDetailsList ?: emptyList()) { item ->
            SkuDetailsItem(item = item, onClick = {

            })
        }
    }
}

@Composable
internal fun SkuDetailsItem(
    item: SkuDetails,
    onClick: (SkuDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {
            onClick.invoke(item)
        },
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Column {
            Row {
               SebangText(text = item.sku)
               SebangText(text = "price!!!")
            }

            SebangText(text = item.description)
        }
    }
}