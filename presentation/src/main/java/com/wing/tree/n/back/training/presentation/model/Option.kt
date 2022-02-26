package com.wing.tree.n.back.training.presentation.model

import android.os.Parcelable
import com.wing.tree.n.back.training.presentation.constant.Rounds
import com.wing.tree.n.back.training.presentation.constant.Speed
import com.wing.tree.n.back.training.presentation.constant.SpeedMode
import kotlinx.parcelize.Parcelize
import com.wing.tree.n.back.training.domain.model.Option as DomainModel

@Parcelize
data class Option(
    override var rounds: Int,
    override var speed: Int,
    override var speedMode: Boolean
) : DomainModel(), Parcelable {
    companion object {
        fun from(model: DomainModel) = Option(
            rounds = model.rounds,
            speed = model.speed,
            speedMode = model.speedMode
        )

        val Default = Option(
            rounds = Rounds.DEFAULT,
            speed = Speed.DEFAULT,
            speedMode = SpeedMode.DEFAULT
        )
    }
}