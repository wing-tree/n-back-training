package com.wing.tree.n.back.training.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.wing.tree.n.back.training.domain.model.Problem
import com.wing.tree.n.back.training.domain.model.RankCheckParameter
import com.wing.tree.n.back.training.domain.model.Ranking
import com.wing.tree.n.back.training.domain.model.Record
import com.wing.tree.n.back.training.domain.usecase.option.UpdateOptionUseCase
import com.wing.tree.n.back.training.domain.usecase.ranking.CheckRankingUseCase
import com.wing.tree.n.back.training.domain.usecase.ranking.RegisterForRankingUseCase
import com.wing.tree.n.back.training.domain.usecase.record.InsertRecordUseCase
import com.wing.tree.n.back.training.presentation.R
import com.wing.tree.n.back.training.presentation.constant.*
import com.wing.tree.n.back.training.presentation.model.Option
import com.wing.tree.n.back.training.presentation.util.quarter
import com.wing.tree.n.back.training.presentation.view.TrainingActivity.State
import com.wing.tree.n.back.training.presentation.view.TrainingParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val checkRankingUseCase: CheckRankingUseCase,
    private val insertRecordUseCase: InsertRecordUseCase,
    private val registerForRankingUseCase: RegisterForRankingUseCase,
    private val updateOptionUseCase: UpdateOptionUseCase,
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val option = savedStateHandle.get<Option>(Extra.OPTION) ?: Option.Default

    private var endTime = 0L
    private var startTime = 0L

    val elapsedTime: Long
        get() = endTime - startTime

    val n = savedStateHandle.get<Int>(Extra.BACK) ?: N.DEFAULT
    val rounds = option.rounds
    val speed = option.speed
    val speedMode = option.speedMode

    private val _title = MutableLiveData("$n-Back")
    val title: LiveData<String> get() = _title

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateOptionUseCase.invoke(UpdateOptionUseCase.Parameter(option))
        }
    }

    val problems: List<Problem> = run {
        val seed = System.currentTimeMillis()

        var intArray = IntArray(option.rounds)
        var previousTrueCount = 0

        repeat(ONE_HUNDRED.quarter) {
            val random = Random(seed).nextInt(From.RANDOM, Until.RANDOM)
            val intRange = IntRange(random, random.plus(Offset.RANDOM))

            var trueCount = 0

            with(IntArray(rounds) { intRange.random() }) {
                repeat(rounds) {
                    if (it >= n) {
                        if (get(it - n) == get(it)) {
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
               array[it] = if (it < n) {
                    null
                } else {
                    intArray[it - n] == intArray[it]
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

    private val _trainingParameter = MutableLiveData(
        TrainingParameter(
            enabled = false,
            visible = true
        )
    )

    val trainingParameter: LiveData<TrainingParameter> get() = _trainingParameter

    private val _state = MutableLiveData<State>(State.RankingRegistration)
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

    fun setEnabled(enabled: Boolean) {
        val value = _trainingParameter.value ?: return

        _trainingParameter.value = TrainingParameter(enabled, value.visible)
    }

    fun setVisible(visible: Boolean) {
        val value = _trainingParameter.value ?: return

        _trainingParameter.value = TrainingParameter(value.enabled, visible)
    }

    fun progress() {
        startTime = System.currentTimeMillis()
    }

    fun complete() {
        endTime = System.currentTimeMillis()

        val viewModel = this

        val record = object : Record() {
            override val n: Int = viewModel.n
            override val problems: List<Problem> = viewModel.problems
            override val rounds: Int = viewModel.rounds
            override val speed: Int = viewModel.speed
            override val timestamp: Long = System.currentTimeMillis()
        }

        insertRecord(record)

        val rankCheckParameter = RankCheckParameter(
            elapsedTime = elapsedTime,
            n = n,
            rounds = rounds
        )

        if (true /*record.isPerfect*/) {
            checkRanking(
                rankCheckParameter, { isSuccessful, rank ->
                    if (isSuccessful) {
                        val suffix = when(rank) {
                            0 -> "st"
                            1 -> "nd"
                            2 -> "rd"
                            else -> "th"
                        }

                        val title = String.format(getApplication<Application>().getString(R.string.ranked), rank.inc(), suffix)

                        _state.value = State.RankingRegistration
                        _title.value = title
                    } else {
                        _state.value = State.Result
                    }
                }
            ) {
                _state.value = State.Result
            }
        } else {
            _state.value = State.Result
        }
    }

    private fun insertRecord(record: Record) {
        viewModelScope.launch(Dispatchers.IO) {
            insertRecordUseCase.invoke(record)
        }
    }

    private fun checkRanking(
        rankCheckParameter: RankCheckParameter,
        onSuccess: (Boolean, Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val parameter = CheckRankingUseCase.Parameter(
                rankCheckParameter,
                onSuccess,
                onFailure
            )

            checkRankingUseCase.invoke(parameter)
        }
    }

    fun registerForRanking(
        name: String,
        country: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val ranking = Ranking(
                country = country,
                elapsedTime = elapsedTime,
                n = n,
                name = name,
                rounds = rounds,
                speed = speed,
                timestamp = Date(),
            )

            val parameter = RegisterForRankingUseCase.Parameter(
                ranking = ranking,
                onSuccess = onSuccess,
                onFailure = { onFailure(it) }
            )

            registerForRankingUseCase.invoke(parameter)
        }
    }
}