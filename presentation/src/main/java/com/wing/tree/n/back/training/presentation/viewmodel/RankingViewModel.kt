package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wing.tree.n.back.training.domain.model.Ranking
import com.wing.tree.n.back.training.domain.usecase.ranking.GetRankingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val getRankingListUseCase: GetRankingListUseCase,
    application: Application
) : AndroidViewModel(application) {
    private var _rankingList = MutableLiveData<List<Ranking>>()
    val rankingList: LiveData<List<Ranking>>
        get() = _rankingList

    fun getRankingList(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getRankingListUseCase.invoke(
                GetRankingListUseCase.Parameter(
                    page = page,
                    pageSize = PAGE_SIZE,
                    onSuccess = {
                        _rankingList.value = it
                    },
                    onFailure = {

                    }
                )
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 10L
    }
}