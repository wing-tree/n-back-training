package com.wing.tree.n.back.training.presentation.adapter.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wing.tree.n.back.training.domain.model.Ranking as DomainModel
import com.wing.tree.n.back.training.presentation.databinding.RankingItemBinding
import java.util.*

class RankingListAdapter : ListAdapter<RankingListAdapter.AdapterItem, RankingListAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(RankingItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val viewBinding: RankingItemBinding): RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: AdapterItem) {
            when(item) {
                is AdapterItem.Ranking -> {
                    with(viewBinding) {
                        textViewRank.text = "${item.rank}"
                        textViewNickname.text = item.nickname
                        textViewElapsedTime.text = "${item.elapsedTime}"
                        textViewN.text = "${item.n}"
                        textViewRounds.text = "${item.rounds}"
                    }
                }
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<AdapterItem>() {
        override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
            return oldItem == newItem
        }
    }

    sealed class AdapterItem {
        abstract val id: String

        data class Ranking(
            override val id: String,
            val elapsedTime: Long,
            val n: Int,
            val nation: String,
            val nickname: String,
            val rank: Int,
            val rounds: Int,
            val timestamp: Date,
        ) : AdapterItem() {
            companion object {
                fun from(rank: Int, domainModel: DomainModel) = with(domainModel) {
                    Ranking(
                        id = id,
                        elapsedTime = elapsedTime,
                        n = n,
                        nation = nation,
                        nickname = nickname,
                        rank = rank,
                        rounds = rounds,
                        timestamp = timestamp
                    )
                }
            }
        }
    }
}