package com.android.daily.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.daily.repository.GoalDetailsRepository

class GoalDetailsViewModelFactory internal constructor(private val repository: GoalDetailsRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = GoalDetailsViewModel(repository) as T
}