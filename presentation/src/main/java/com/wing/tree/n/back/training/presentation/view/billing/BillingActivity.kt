package com.wing.tree.n.back.training.presentation.view.billing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.SkuDetails
import com.wing.tree.n.back.training.domain.util.`is`
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.*
import com.wing.tree.n.back.training.presentation.ui.theme.horizontalPadding
import com.wing.tree.n.back.training.presentation.ui.theme.verticalPadding
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar
import com.wing.tree.n.back.training.presentation.viewmodel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

@AndroidEntryPoint
class BillingActivity : ComponentActivity() {
    private val viewModel by viewModels<BillingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val skuDetailsList by viewModel.skuDetailsList.observeAsState()

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

                        SkuDetailsList(skuDetailsList ?: emptyList(), Modifier.fillMaxWidth()) {
                            viewModel.purchase(this@BillingActivity, it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun SkuDetailsList(skuDetailsList: List<SkuDetails>, modifier: Modifier = Modifier, onClick: (SkuDetails) -> Unit) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(12.dp)) {
        items(skuDetailsList) { item ->
            SkuDetailsItem(item = item, onClick = onClick, modifier = Modifier.fillMaxWidth())
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
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick(item) }
                .horizontalPadding(16.dp)
                .verticalPadding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SebangText(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val price = "${item.priceCurrencyCode} ${item.price.filter { 
                        it.`is`(',') || it.isDigit() 
                    }}"

                    SebangText(
                        text = price,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            SebangText(text = item.description)
        }
    }
}

private val SkuDetails.jsonObject get() = JSONObject(originalJson)

private val SkuDetails.name: String get() = try {
    jsonObject.getString("name")
} catch (e: JSONException) {
    Timber.e(e)
    title
}