package com.wing.tree.n.back.training.presentation.model

import android.os.Parcelable
import com.wing.tree.n.back.training.domain.model.Problem as DomainModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Problem(
    override val solution: Boolean?,
    override val number: Int,
    override var answer: Boolean?
) : DomainModel(), Parcelable {
    companion object {
        fun from(model: DomainModel) = with(model) {
            Problem(
                solution = solution,
                number = number,
                answer = answer
            )
        }
    }
}