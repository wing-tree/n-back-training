package com.wing.tree.n.back.training.presentation.view.ranking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.wing.tree.n.back.training.presentation.adapter.ranking.RankingFragmentStateAdapter
import com.wing.tree.n.back.training.presentation.databinding.ActivityRankingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityRankingBinding.inflate(layoutInflater) }
    private val rankingFragmentStateAdapter = RankingFragmentStateAdapter(this)

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            val text = "${position.inc()}/5"

            viewBinding.textViewPageIndicator.text = text
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        bind()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    private fun bind() {
        with(viewBinding) {
            viewPager2.apply {
                registerOnPageChangeCallback(onPageChangeCallback)
                adapter = rankingFragmentStateAdapter
                isUserInputEnabled = false
                offscreenPageLimit = 4
            }

            imageViewNext.setOnClickListener {
                val currentItem = viewPager2.currentItem

                if (currentItem < 4) {
                    viewPager2.setCurrentItem(currentItem.inc(), true)
                }
            }

            viewBinding.textViewPageIndicator.text = "1/5"

            imageViewPrevious.setOnClickListener {
                val currentItem = viewPager2.currentItem

                if (currentItem > 0) {
                    viewPager2.setCurrentItem(currentItem.dec(), true)
                }
            }
        }
    }
}