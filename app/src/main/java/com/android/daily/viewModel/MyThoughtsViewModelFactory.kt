package com.android.daily.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.daily.repository.MyThoughtsRepository

class MyThoughtsViewModelFactory internal constructor(private val repository: MyThoughtsRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = MyThoughtsViewModel(repository) as T
}