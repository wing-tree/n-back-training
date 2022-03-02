package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.ui.theme.ApplicationTheme
import com.wing.tree.n.back.training.presentation.view.Header

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
                        Header(title = getString(R.string.ranking))
                    }
                }
            }
        }
    }
}