package com.wing.tree.n.back.training.presentation.view.ranking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wing.tree.n.back.training.presentation.adapter.ranking.RankingFragmentStateAdapter
import com.wing.tree.n.back.training.presentation.databinding.ActivityRankingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityRankingBinding.inflate(layoutInflater) }
    private val rankingFragmentStateAdapter = RankingFragmentStateAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        bind()
    }

    private fun bind() {
        with(viewBinding) {
            viewPager2.apply {
                adapter = rankingFragmentStateAdapter
                offscreenPageLimit = 4
            }
        }
    }
}