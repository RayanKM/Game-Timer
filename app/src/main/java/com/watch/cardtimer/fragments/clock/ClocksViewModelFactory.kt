package com.watch.cardtimer.fragments.clock

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.watch.cardtimer.fragments.clock.ClockViewModel

class ClocksViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClockViewModel::class.java)) {
            return ClockViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}