package com.wing.tree.n.back.training.presentation.model

import android.os.Parcelable
import com.wing.tree.n.back.training.presentation.util.notNull
import com.wing.tree.n.back.training.domain.model.Record as DomainModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Record(
    override val id: Long,
    override val n: Int,
    override val elapsedTime: Long,
    override val rounds: Int,
    override val speed: Int,
    override val timestamp: Long,
    override val problems: List<Problem>
) : DomainModel(), Parcelable {
    val result: String
        get() = "${problems.filter { it.correct }.count()}/${problems.filter { it.solution.notNull }.count()}"

    companion object {
        fun from(model: DomainModel) = with(model) {
            Record(
                id = id,
                n = n,
                elapsedTime = elapsedTime,
                rounds = rounds,
                speed = speed,
                timestamp = timestamp,
                problems = model.problems.map { Problem.from(it) }
            )
        }
    }
}