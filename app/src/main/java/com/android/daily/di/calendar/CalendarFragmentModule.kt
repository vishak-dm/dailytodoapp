package com.android.daily.di.calendar

import com.android.daily.ui.MyCalendarFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CalendarFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMyCalendarFragment(): MyCalendarFragment
}