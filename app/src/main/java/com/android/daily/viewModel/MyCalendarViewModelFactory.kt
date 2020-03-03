package com.android.daily.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.daily.repository.MyCalendarRepository
import com.android.daily.repository.MyGoalsRepository

class MyCalendarViewModelFactory internal constructor(private val repository: MyCalendarRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MyCalendarViewModel(repository) as T
}