package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int): ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    init {
        _score.postValue(finalScore)
    }
}