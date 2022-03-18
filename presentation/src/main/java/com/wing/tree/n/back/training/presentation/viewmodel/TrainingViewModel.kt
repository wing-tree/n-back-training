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
import com.wing.tree.n.back.training.presentation.constant.Random.FROM
import com.wing.tree.n.back.training.presentation.constant.Random.OFFSET
import com.wing.tree.n.back.training.presentation.constant.Random.UNTIL
import com.wing.tree.n.back.training.presentation.model.Option
import com.wing.tree.n.back.training.presentation.util.`is`
import com.wing.tree.n.back.training.presentation.util.quarter
import com.wing.tree.n.back.training.presentation.view.training.TrainingActivity.State
import com.wing.tree.n.back.training.presentation.view.training.ReadyParameter
import com.wing.tree.n.back.training.presentation.view.training.TrainingParameter
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

    val n = savedStateHandle.get<Int>(Extra.N) ?: N.DEFAULT
    val rounds = option.rounds
    val speed = option.speed
    val speedMode = option.speedMode

    private val _state = MutableLiveData<State>(State.Ready)
    val state: LiveData<State> get() = _state

    private val _title = MutableLiveData("$n-Back")
    val title: LiveData<String> get() = _title

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateOptionUseCase.invoke(UpdateOptionUseCase.Parameter(option))
        }
    }

    val problems: List<Problem> = run {
        var numbers = IntArray(option.rounds)
        var previousTrueCount = 0

        repeat(ONE_HUNDRED.quarter) {
            val seed = System.currentTimeMillis()
            val random = Random(seed).nextInt(FROM, UNTIL)
            val intRange = IntRange(random, random.plus(OFFSET))

            var trueCount = 0

            with(IntArray(rounds) { intRange.random() }) {
                repeat(rounds) {
                    if (it >= n) {
                        if (get(it - n).`is`(get(it))) {
                            ++trueCount
                        }
                    }
                }

                if (previousTrueCount < trueCount) {
                    numbers = this.clone()
                    previousTrueCount = trueCount
                }
            }
        }

        val solutions by lazy {
            Array(rounds) {
                if (it < n) {
                    null
                } else {
                    numbers[it - n].`is`(numbers[it])
                }
            }
        }

        numbers.zip(solutions) { number, solution ->
            object : Problem() {
                override val number: Int
                    get() = number
                override val solution: Boolean?
                    get() = solution
                override var answer: Boolean? = null
            }
        }
    }

    private val _readyParameter = MutableLiveData(
        ReadyParameter(
            text = getApplication<Application>().getString(R.string.ready)
        )
    )

    val readyParameter: LiveData<ReadyParameter> get() = _readyParameter

    private val _trainingParameter = MutableLiveData(
        TrainingParameter(
            enabled = false,
            visible = true
        )
    )

    val trainingParameter: LiveData<TrainingParameter> get() = _trainingParameter

    fun setEnabled(enabled: Boolean) {
        val value = _trainingParameter.value ?: return

        _trainingParameter.value = TrainingParameter(enabled, value.visible)
    }

    fun setVisible(visible: Boolean) {
        val value = _trainingParameter.value ?: return

        _trainingParameter.value = TrainingParameter(value.enabled, visible)
    }

    fun ready() {
        viewModelScope.launch {
            delay(ONE_SECOND)

            _readyParameter.value = ReadyParameter(text = getApplication<Application>().getString(R.string.start))

            delay(ONE_SECOND)

            _state.value = State.Training
        }
    }

    fun start() {
        startTime = System.nanoTime()
    }

    fun end() {
        endTime = System.nanoTime()

        val record = object : Record() {
            override val elapsedTime: Long = this@TrainingViewModel.elapsedTime
            override val n: Int = this@TrainingViewModel.n
            override val problems: List<Problem> = this@TrainingViewModel.problems
            override val rounds: Int = this@TrainingViewModel.rounds
            override val speed: Int = this@TrainingViewModel.speed
            override val timestamp: Long = System.currentTimeMillis()
        }

        insertRecord(record)

        val rankCheckParameter = RankCheckParameter(
            elapsedTime = elapsedTime,
            n = n,
            rounds = rounds
        )

        if (checkRankingRegistrationCondition(record)) {
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

                        _state.value = State.Ranking
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
            insertRecordUseCase.invoke(InsertRecordUseCase.Parameter(record))
        }
    }

    private fun checkRankingRegistrationCondition(record: Record): Boolean {
        if (n < N.DEFAULT.inc()) return false
        if (rounds < Rounds.DEFAULT) return false
        if (speedMode.not()) return false
        if (record.perfect.not()) return false

        return true
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