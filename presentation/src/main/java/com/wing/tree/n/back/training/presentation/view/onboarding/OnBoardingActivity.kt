package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.adapter.OnBoardingFragmentStateAdapter
import com.wing.tree.n.back.training.presentation.databinding.ActivityOnBoardingBinding
import com.wing.tree.n.back.training.presentation.util.`is`

class OnBoardingActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityOnBoardingBinding.inflate(layoutInflater) }
    private val onBoardingFragmentStateAdapter by lazy{ OnBoardingFragmentStateAdapter(this) }
    private val onPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                with(viewBinding.materialButtonNext) {
                    if (position.`is`(OnBoardingFragmentStateAdapter.ITEM_COUNT.dec())) {
                        text = getString(R.string.done)

                        setOnClickListener {
                            finish()
                        }
                    } else {
                        text = getString(R.string.next)

                        setOnClickListener {
                            val currentItem = viewBinding.viewPager2.currentItem

                            if (currentItem <= OnBoardingFragmentStateAdapter.ITEM_COUNT) {
                                viewBinding.viewPager2.setCurrentItem(currentItem.inc(), true)
                            }
                        }
                    }
                }
            }
        }
    }

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
                registerOnPageChangeCallback(onPageChangeCallback)
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