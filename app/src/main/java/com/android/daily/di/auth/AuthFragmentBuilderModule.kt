package com.android.daily.di.auth

import com.android.daily.ui.LoginFragment
import com.android.daily.ui.MainFragment
import com.android.daily.ui.RegisterFragment
import com.android.daily.ui.UserDetailsFragement
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeUserDetailsFragment(): UserDetailsFragement


}