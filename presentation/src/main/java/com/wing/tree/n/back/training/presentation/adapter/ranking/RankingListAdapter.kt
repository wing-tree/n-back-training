package com.wing.tree.n.back.training.presentation.adapter.ranking

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wing.tree.n.back.training.domain.model.Ranking as DomainModel
import com.wing.tree.n.back.training.presentation.databinding.RankingItemBinding
import java.util.*

class RankingListAdapter : ListAdapter<RankingListAdapter.AdapterItem, RankingListAdapter.ViewHolder>(DiffCallback()) {
    inner class ViewHolder(viewBinding: RankingItemBinding): RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
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
            val rounds: Int,
            val timestamp: Date,
        ) : AdapterItem() {
            companion object {
                fun from(domainModel: DomainModel) = with(domainModel) {
                    Ranking(
                        id = id,
                        elapsedTime = elapsedTime,
                        n = n,
                        nation = nation,
                        nickname = nickname,
                        rounds = rounds,
                        timestamp = timestamp
                    )
                }
            }
        }
    }
}