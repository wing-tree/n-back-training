package com.wing.tree.n.back.training.domain.model

enum class SortBy(val value: Int) {
    Newest(0), Oldest(1);

    companion object {
        val Default = Newest.value

        fun valueOf(value: Int) = values().first { it.value == value }
    }
}