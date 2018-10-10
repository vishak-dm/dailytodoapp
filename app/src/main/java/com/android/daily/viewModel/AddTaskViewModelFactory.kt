package com.android.daily.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.daily.repository.AddTaskRepository

class AddTaskViewModelFactory internal constructor(private val repository: AddTaskRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = AddTaskViewModel(repository) as T
}