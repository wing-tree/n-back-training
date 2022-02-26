package com.wing.tree.n.back.training.presentation.model

import androidx.annotation.DrawableRes
import com.wing.tree.n.back.training.presentation.constant.BLANK

sealed class Menu {
    object Divider : Menu()

    data class Item(
        @DrawableRes val icon: Int,
        val title: String,
        val subtext: String = BLANK,
        val onClick: (() -> Unit)
    ) : Menu()

    data class Switch(
        val checked: Boolean,
        val textOff: String,
        val textOn: String,
        val onCheckedChanged: ((Boolean) -> Unit)
    ) : Menu()
}