package com.example.android.guesstheword.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.guesstheword.screens.game.GameViewModel
/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
        private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(GameViewModel::class.java) ->
                        GameViewModel()
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}