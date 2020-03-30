package com.example.android.guesstheword.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
inline fun <reified T: ViewModel> Fragment.getViewModelFactory(): T {
    return ViewModelProvider(this, ViewModelFactory(requireContext().applicationContext)).get(T::class.java)
}

inline fun <reified T: ViewModel> FragmentActivity.getViewModelFactory(): T {
    return ViewModelProvider(this, ViewModelFactory(applicationContext)).get(T::class.java)
}