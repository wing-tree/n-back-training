package com.wing.tree.n.back.training.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wing.tree.n.back.training.presentation.view.onboarding.OnBoardingPageFourFragment
import com.wing.tree.n.back.training.presentation.view.onboarding.OnBoardingPageOneFragment
import com.wing.tree.n.back.training.presentation.view.onboarding.OnBoardingPageThreeFragment
import com.wing.tree.n.back.training.presentation.view.onboarding.OnBoardingPageTwoFragment

class OnBoardingFragmentStateAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> OnBoardingPageOneFragment()
            1 -> OnBoardingPageTwoFragment()
            2 -> OnBoardingPageThreeFragment()
            3 -> OnBoardingPageFourFragment()
            else -> throw IllegalStateException("position :$position")
        }
    }

    companion object {
        const val ITEM_COUNT = 4
    }
}