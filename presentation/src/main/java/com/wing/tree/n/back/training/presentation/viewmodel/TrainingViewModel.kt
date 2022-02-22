package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.wing.tree.n.back.training.domain.model.Problem
import com.wing.tree.n.back.training.domain.model.Ranking
import com.wing.tree.n.back.training.domain.model.Record
import com.wing.tree.n.back.training.domain.usecase.option.UpdateOptionUseCase
import com.wing.tree.n.back.training.domain.usecase.ranking.RegisterRankingUseCase
import com.wing.tree.n.back.training.domain.usecase.record.InsertRecordUseCase
import com.wing.tree.n.back.training.presentation.constant.*
import com.wing.tree.n.back.training.presentation.model.Option
import com.wing.tree.n.back.training.presentation.util.quarter
import com.wing.tree.n.back.training.presentation.view.TrainingActivity.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates
import kotlin.random.Random

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val insertRecordUseCase: InsertRecordUseCase,
    private val registerRankingUseCase: RegisterRankingUseCase,
    private val updateOptionUseCase: UpdateOptionUseCase,
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val option = savedStateHandle.get<Option>(Extra.OPTION) ?: Option.Default

    private var endTime by Delegates.notNull<Long>()
    private var startTime by Delegates.notNull<Long>()

    val back = savedStateHandle.get<Int>(Extra.BACK) ?: Back.DEFAULT
    val rounds = option.rounds
    val speed = option.speed

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateOptionUseCase.invoke(option)
        }
    }

    val problemList: List<Problem> = run {
        val seed = System.currentTimeMillis()

        var intArray = IntArray(option.rounds)
        var previousTrueCount = 0

        repeat(ONE_HUNDRED.quarter) {
            val random = Random(seed).nextInt(From.RANDOM, Until.RANDOM)
            val intRange = IntRange(random, random.plus(Offset.RANDOM))

            var trueCount = 0

            with(IntArray(rounds) { intRange.random() }) {
                repeat(rounds) {
                    if (it >= back) {
                        if (get(it - back) == get(it)) {
                            ++trueCount
                        }
                    }
                }

                if (previousTrueCount < trueCount) {
                    intArray = this.clone()
                    previousTrueCount = trueCount
                }
            }
        }

        val solutionArray by lazy {
            val array = arrayOfNulls<Boolean?>(rounds)

            repeat(rounds) {
               array[it] = if (it < back) {
                    null
                } else {
                    intArray[it - back] == intArray[it]
                }
            }

            array
        }

        intArray.zip(solutionArray) { value, solution ->
            object : Problem() {
                override val solution: Boolean?
                    get() = solution
                override val number: Int
                    get() = value
                override var answer: Boolean? = null
            }
        }
    }

    private val _countDown = MutableLiveData(From.COUNT_DOWN)
    val countDown: LiveData<Int?> get() = _countDown

    private val _enabled = MutableLiveData(true)
    val enabled: LiveData<Boolean> get() = _enabled

    private val _isVisible = MutableLiveData(true)
    val isVisible: LiveData<Boolean> get() = _isVisible

    private val _round = MutableLiveData(0)
    val round: LiveData<Int> get() = _round

    private val _state = MutableLiveData<State>(State.Ready)
    val state: LiveData<State> get() = _state

    fun ready() {
        viewModelScope.launch {
            repeat(From.COUNT_DOWN) {
                delay(ONE_SECOND)

                _countDown.value = _countDown.value?.dec()
            }

            delay(ONE_SECOND)

            _state.value = State.Training
        }
    }

    fun setIsVisible(value: Boolean) {
        _isVisible.value = value
    }

    fun progress() {
        startTime = System.nanoTime()
//        if (inProgress.compareAndSet(false, true)) {
//            viewModelScope.launch {
//                repeat(rounds) {
//                    _round.value = it
//                    _isVisible.value = true
//
//                    _enabled.value = n <= it
//
//                    delay(ONE_SECOND.times(speed))
//
//                    _isVisible.value = false
//
//                    delay(ONE_SECOND.quarter)
//                }
//
//                _state.value = State.Finish
//            }
//        }
    }

    fun complete() {
        endTime = System.nanoTime()

        insertRecord()

        _state.value = State.Result

        registerRanking()
    }

    private fun insertRecord() {
        val viewModel = this

        viewModelScope.launch(Dispatchers.IO) {
            insertRecordUseCase.invoke(
                object : Record() {
                    override val n: Int = viewModel.back
                    override val problemList: List<Problem> = viewModel.problemList
                    override val rounds: Int = viewModel.rounds
                    override val speed: Int = viewModel.speed
                    override val timestamp: Long = System.currentTimeMillis()
                }
            )
        }
    }

    fun checkRankingRegistrationConditions(): Boolean {
        if (back < 4) return false
        if (option.rounds < 30) return false

        return true
    }

    fun registerRanking() {
        viewModelScope.launch(Dispatchers.IO) {
            val ranking = Ranking(
                n = back,
                elapsedTime = endTime - startTime,
                nation = "",
                nickname = "nickname",
                rounds = rounds,
                timestamp = Date(),
            )

            val parameter = RegisterRankingUseCase.Parameter(
                ranking = ranking,
                onSuccess = {
                            println("zioziosssssss, $it")
                },
                onFailure = {
                    Log.e("sjk", it.message ?: "")
                }
            )

            registerRankingUseCase.invoke(parameter)
        }
    }
}