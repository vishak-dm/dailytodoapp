package com.android.daily.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.daily.repository.GoalDetailsRepository

class GoalDetailsViewModelFactory internal constructor(private val repository: GoalDetailsRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = GoalDetailsViewModel(repository) as T
}