package com.android.daily.utilities

import android.content.Context
import com.android.daily.R
import com.android.daily.repository.UserRepository
import com.android.daily.viewModel.AuthenticationViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


object InjectorUtils {
    private fun getUserRepository(): UserRepository {
        return UserRepository.getInstance()
    }

    fun provideAuthenticationViewModelFactory(): AuthenticationViewModelFactory {
        return AuthenticationViewModelFactory(getUserRepository())
    }
}