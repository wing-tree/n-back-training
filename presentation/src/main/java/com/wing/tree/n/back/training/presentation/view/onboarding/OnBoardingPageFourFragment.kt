package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.ui.theme.horizontalPadding
import com.wing.tree.n.back.training.presentation.view.shared.SebangText
import com.wing.tree.n.back.training.presentation.view.shared.TopAppbar

class OnBoardingPageFourFragment : Fragment() {
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
                            TopAppbar(title = getString(R.string.ranking))

                            Spacer(modifier = Modifier.height(24.dp))

                            LazyColumn {
                                item {
                                    Column(modifier = Modifier.horizontalPadding(24.dp)) {
                                        SebangText(
                                            text = "1. ${getString(R.string.ranking1)}",
                                            textAlign = TextAlign.Start
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        SebangText(
                                            text = "2. ${getString(R.string.ranking2)}",
                                            textAlign = TextAlign.Start
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        SebangText(
                                            text = "3. ${getString(R.string.ranking3)}",
                                            textAlign = TextAlign.Start
                                        )
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