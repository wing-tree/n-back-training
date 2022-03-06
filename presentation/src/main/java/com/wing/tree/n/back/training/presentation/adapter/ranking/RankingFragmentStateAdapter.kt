package com.wing.tree.n.back.training.presentation.adapter.ranking

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wing.tree.n.back.training.presentation.view.ranking.RankingFragment

class RankingFragmentStateAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        return RankingFragment.newInstance(position)
    }

    companion object {
        const val PAGE_COUNT = 5
    }
}