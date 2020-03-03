package com.android.daily.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.daily.repository.AddTaskRepository
import com.android.daily.repository.TodayTaskRepository

class AddMitViewModelFactory internal constructor(private val repository: TodayTaskRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = AddMitViewModel(repository) as T
}