package com.android.daily.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.daily.repository.MyCalendarRepository
import com.android.daily.repository.MyGoalsRepository

class MyCalendarViewModelFactory internal constructor(private val repository: MyCalendarRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MyCalendarViewModel(repository) as T
}