package com.android.daily.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.daily.repository.UserRepository

class AuthenticationViewModelFactory internal constructor(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = AuthenticationViewModel(repository) as T
}