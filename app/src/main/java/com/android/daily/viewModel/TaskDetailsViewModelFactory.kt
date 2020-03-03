package com.android.daily.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.daily.repository.TaskDetailsRepository

class TaskDetailsViewModelFactory internal constructor(private val repository: TaskDetailsRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = TaskDetailsViewModel(repository) as T
}