package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import com.wing.tree.n.back.training.presentation.adapter.OnBoardingFragmentStateAdapter
import com.wing.tree.n.back.training.presentation.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityOnBoardingBinding.inflate(layoutInflater) }
    private val onBoardingFragmentStateAdapter by lazy{ OnBoardingFragmentStateAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        bind()
    }

    private fun bind() {
        with(viewBinding) {
            imageViewClose.setOnClickListener {
                finish()
            }

            viewPager2.apply {
                adapter = onBoardingFragmentStateAdapter
            }

            circleIndicator3.setViewPager(viewPager2)

            materialButtonPrevious.setOnClickListener {
                val currentItem = viewPager2.currentItem

                if (currentItem > 0) {
                    viewPager2.setCurrentItem(currentItem.dec(), true)
                }
            }

            materialButtonNext.setOnClickListener {
                val currentItem = viewPager2.currentItem

                if (currentItem <= OnBoardingFragmentStateAdapter.ITEM_COUNT) {
                    viewPager2.setCurrentItem(currentItem.inc(), true)
                }
            }
        }
    }
}