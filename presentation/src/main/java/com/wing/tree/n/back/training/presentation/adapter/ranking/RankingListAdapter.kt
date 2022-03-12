package com.wing.tree.n.back.training.presentation.adapter.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wing.tree.n.back.training.domain.model.Ranking as DomainModel
import com.wing.tree.n.back.training.presentation.databinding.RankingItemBinding
import com.wing.tree.n.back.training.presentation.util.flagEmoji
import java.text.SimpleDateFormat
import java.util.*

class RankingListAdapter(private val page: Int, private val pageSize: Int) : ListAdapter<RankingListAdapter.AdapterItem, RankingListAdapter.ViewHolder>(DiffCallback()) {
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
                        val rank = "${item.rank.inc() + page.times(pageSize)}"
                        val nBack = "${item.n}-Back"
                        val timestamp = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(item.timestamp)

                        textViewRank.text = rank
                        textViewCountry.text = item.country.flagEmoji
                        textViewName.text = item.name
                        textViewElapsedTimeValue.text = String.format("%.3f", item.elapsedTime / 1_000_000_000.0)
                        textViewNBack.text = nBack
                        textViewRoundsValue.text = "${item.rounds}"
                        textViewTimestamp.text = timestamp
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
            val country: String,
            val elapsedTime: Long,
            val n: Int,
            val name: String,
            val rank: Int,
            val rounds: Int,
            val speed: Int,
            val timestamp: Date,
        ) : AdapterItem() {
            companion object {
                fun from(rank: Int, domainModel: DomainModel) = with(domainModel) {
                    Ranking(
                        country = country,
                        id = id,
                        elapsedTime = elapsedTime,
                        n = n,
                        name = name,
                        rank = rank,
                        rounds = rounds,
                        speed = speed,
                        timestamp = timestamp
                    )
                }
            }
        }
    }
}