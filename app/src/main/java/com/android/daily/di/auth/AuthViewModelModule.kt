package com.android.daily.di.auth

import androidx.lifecycle.ViewModel
import com.android.daily.di.ViewModelKey
import com.android.daily.viewModel.AuthenticationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    abstract fun bindsAuthViewModel(authViewModel : AuthenticationViewModel) : ViewModel
}