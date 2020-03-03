package com.android.daily.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.daily.repository.AddNotesRepository

class AddNotesViewModelFactory internal constructor(private val repository: AddNotesRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = AddNotesViewModel(repository) as T
}