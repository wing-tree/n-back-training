package com.wing.tree.n.back.training.presentation.model

import android.os.Parcelable
import com.wing.tree.n.back.training.domain.model.Problem as DomainModel
import kotlinx.parcelize.Parcelize

@Parcelize
class Problem(
    override val number: Int,
    override val solution: Boolean?,
    override var answer: Boolean?
) : DomainModel(), Parcelable {
    companion object {
        fun from(domainModel: DomainModel) = with(domainModel) {
            Problem(
                number = number,
                solution = solution,
                answer = answer
            )
        }
    }
}