package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wing.tree.n.back.training.domain.model.Ranking
import com.wing.tree.n.back.training.domain.usecase.ranking.GetRankingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val getRankingsUseCase: GetRankingsUseCase,
    application: Application
) : AndroidViewModel(application) {
    private var _rankings = MutableLiveData<List<Ranking>>()
    val rankings: LiveData<List<Ranking>>
        get() = _rankings

    fun getRankings(page: Int, onFailure: (Exception) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            getRankingsUseCase.invoke(
                GetRankingsUseCase.Parameter(
                    page = page,
                    pageSize = PAGE_SIZE,
                    onSuccess = { _rankings.postValue(it) },
                    onFailure = onFailure
                )
            )
        }
    }

    companion object {
        const val PAGE_SIZE = 10L
    }
}