package com.android.daily.di

import androidx.lifecycle.ViewModelProvider
import com.android.daily.viewModel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindsviewModelfactoryprovider(viewModelProviderFactory: ViewModelProviderFactory)
            : ViewModelProvider.Factory

}