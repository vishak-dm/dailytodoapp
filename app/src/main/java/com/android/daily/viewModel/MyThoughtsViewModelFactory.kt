package com.android.daily.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.daily.repository.MyThoughtsRepository

class MyThoughtsViewModelFactory internal constructor(private val repository: MyThoughtsRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = MyThoughtsViewModel(repository) as T
}