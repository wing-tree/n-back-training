package com.wing.tree.n.back.training.presentation.model

import android.os.Parcelable
import com.wing.tree.n.back.training.domain.model.Record as DomainModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Record(
    override val back: Int,
    override val rounds: Int,
    override val speed: Int,
    override val time: Long,
    override val problemList: List<Problem>
) : DomainModel(), Parcelable {
    companion object {
        fun from(model: DomainModel) = with(model) {
            Record(
                back = back,
                rounds = rounds,
                speed = speed,
                time = time,
                problemList = model.problemList.map { Problem.from(it) }
            )
        }
    }
}