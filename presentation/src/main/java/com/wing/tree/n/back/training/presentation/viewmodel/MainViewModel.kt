package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wing.tree.n.back.training.domain.usecase.Result
import com.wing.tree.n.back.training.domain.usecase.option.GetOptionUseCase
import com.wing.tree.n.back.training.presentation.model.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOptionUseCase: GetOptionUseCase,
    application: Application
) : AndroidViewModel(application) {
    val option: Option get() = runBlocking {
        return@runBlocking withTimeoutOrNull(250L) {
            when (val option = getOptionUseCase.invoke(Unit)) {
                is Result.Error -> Option.Default
                is Result.Loading -> Option.Default
                is Result.Success -> Option.from(option.data)
            }
        } ?: Option.Default
    }
}