package com.wing.tree.n.back.training.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.material.LocalContentColor
import androidx.compose.ui.graphics.Color
import com.wing.tree.n.back.training.presentation.constant.BLANK
import com.wing.tree.n.back.training.presentation.ui.theme.PrimaryVariant
import com.wing.tree.n.back.training.presentation.ui.theme.Teal900

sealed class Menu {
    object Divider : Menu()

    data class Header(
        val title: String
    ) : Menu()

    data class Item(
        @DrawableRes val icon: Int,
        val title: String,
        val subtext: String = BLANK,
        val tint: Color = PrimaryVariant,
        val onClick: (() -> Unit)
    ) : Menu()

    data class Switch(
        val checked: Boolean,
        val textOff: String,
        val textOn: String,
        val onCheckedChanged: ((Boolean) -> Unit)
    ) : Menu()

    data class Text(
        val text: String
    ) : Menu()
}