package com.wing.tree.n.back.training.presentation.view.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wing.tree.n.back.training.presentation.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityOnBoardingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}