package com.android.daily.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.daily.repository.TaskDetailsRepository

class TaskTimerViewModelFactory internal constructor(private val repository: TaskDetailsRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = TaskTimerViewModel(repository) as T
}