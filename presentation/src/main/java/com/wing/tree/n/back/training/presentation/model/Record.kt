package com.wing.tree.n.back.training.presentation.model

import android.os.Parcelable
import com.wing.tree.n.back.training.domain.model.Record as DomainModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Record(
    override val n: Int,
    override val rounds: Int,
    override val speed: Int,
    override val timestamp: Long,
    override val problems: List<Problem>
) : DomainModel(), Parcelable {
    companion object {
        fun from(model: DomainModel) = with(model) {
            Record(
                n = n,
                rounds = rounds,
                speed = speed,
                timestamp = timestamp,
                problems = model.problems.map { Problem.from(it) }
            )
        }
    }
}