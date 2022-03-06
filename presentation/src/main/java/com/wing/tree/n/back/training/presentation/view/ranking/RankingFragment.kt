package com.wing.tree.n.back.training.presentation.view.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wing.tree.n.back.training.presentation.adapter.ranking.RankingListAdapter
import com.wing.tree.n.back.training.presentation.constant.PACKAGE_NAME
import com.wing.tree.n.back.training.presentation.databinding.FragmentRankingBinding
import com.wing.tree.n.back.training.presentation.util.int
import com.wing.tree.n.back.training.presentation.viewmodel.RankingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingFragment : Fragment() {
    private var _viewBinding: FragmentRankingBinding? = null
    private val viewBinding: FragmentRankingBinding
        get() = _viewBinding!!

    private val viewModel by viewModels<RankingViewModel>()

    private val page by lazy { arguments?.getInt(Key.PAGE) ?: 0 }
    private val rankingListAdapter by lazy { RankingListAdapter(page, RankingViewModel.PAGE_SIZE.int) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentRankingBinding.inflate(inflater, container, false)

        bind()

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getRankings(page) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun bind() {
        with(viewBinding) {
            recyclerView.apply {
                adapter = rankingListAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun initData() {
        viewModel.rankings.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                viewBinding.linearLayoutEmptyState.isVisible = false

                rankingListAdapter.submitList(
                    list.mapIndexed { rank, ranking ->
                        RankingListAdapter.AdapterItem.Ranking.from(rank, ranking)
                    }
                )
            } else {
                viewBinding.linearLayoutEmptyState.isVisible = true
            }
        }
    }

    companion object {
        private object Key {
            private const val OBJECT_NAME = "Key"

            const val PAGE = "$PACKAGE_NAME.$OBJECT_NAME.PAGE"
        }

        fun newInstance(page: Int): RankingFragment {
            val bundle  = Bundle().apply {
                putInt(Key.PAGE, page)
            }

            return RankingFragment().apply {
                arguments = bundle
            }
        }
    }
}