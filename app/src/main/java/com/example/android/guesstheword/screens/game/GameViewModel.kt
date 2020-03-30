package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import timber.log.Timber

class GameViewModel: ViewModel() {
    private val timer: CountDownTimer

    private val _isGameFinished = MutableLiveData(false)
    val isGameFinished: LiveData<Boolean> = _isGameFinished

    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String> = _word

    // The current score
    private var _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    // The current score
    private var _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long> = _currentTime

    val currentTimeString = Transformations.map(currentTime) {
        DateUtils.formatElapsedTime(it)
    }

    private val _buzz = MutableLiveData<BuzzType>()
    val buzz: LiveData<BuzzType> = _buzz

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Timber.i("GameViewModel created!")
        resetList()
        nextWord()

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _isGameFinished.postValue(true)
                _buzz.postValue(BuzzType.GAME_OVER)
            }

            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / ONE_SECOND
                _currentTime.postValue(timeLeft)
                if (timeLeft < PANIC_COUNTDOWN) _buzz.postValue(BuzzType.COUNTDOWN_PANIC)
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Timber.i("GameViewModel cleared!")
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.postValue(score.value?.minus(1))
        nextWord()
    }

    fun onCorrect() {
        _score.postValue(score.value?.plus(1))
        nextWord()
        _buzz.postValue(BuzzType.CORRECT)
    }

    fun gameFinishedCompleted() = _isGameFinished.postValue(false)

    fun onCompleteBuzzing() = _buzz.postValue(BuzzType.NO_BUZZ)

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }

        _word.postValue(wordList.removeAt(0))
    }

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val PANIC_COUNTDOWN = 10L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L

        private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
        private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
        private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
        private val NO_BUZZ_PATTERN = longArrayOf(0)
    }
}