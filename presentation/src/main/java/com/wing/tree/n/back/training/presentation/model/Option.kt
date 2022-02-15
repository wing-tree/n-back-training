package com.wing.tree.n.back.training.presentation.model

import android.os.Parcelable
import com.wing.tree.n.back.training.presentation.constant.Back
import com.wing.tree.n.back.training.presentation.constant.Rounds
import com.wing.tree.n.back.training.presentation.constant.Speed
import kotlinx.parcelize.Parcelize
import com.wing.tree.n.back.training.domain.model.Option as DomainModel

@Parcelize
data class Option(
    override var n: Int,
    override var rounds: Int,
    override var speed: Int
) : DomainModel(), Parcelable {
    companion object {
        fun from(model: DomainModel) = Option(
            n = model.n,
            rounds = model.rounds,
            speed = model.speed
        )

        val Default = Option(
            n = Back.DEFAULT,
            rounds = Rounds.DEFAULT,
            speed = Speed.DEFAULT,
        )
    }
}