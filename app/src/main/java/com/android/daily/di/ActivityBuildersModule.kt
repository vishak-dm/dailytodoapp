package com.android.daily.di

import com.android.daily.di.auth.AuthFragmentBuilderModule
import com.android.daily.di.auth.AuthViewModelModule
import com.android.daily.di.calendar.CalendarFragmentModule
import com.android.daily.di.calendar.CalendarViewModelModule
import com.android.daily.di.goals.GoalFragmentModule
import com.android.daily.di.goals.GoalsViewModelModule
import com.android.daily.di.notes.NotesFragmentModule
import com.android.daily.di.notes.NotesViewModelModule
import com.android.daily.di.tasks.TaskFragmentModule
import com.android.daily.di.tasks.TaskViewModelModule
import com.android.daily.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    //inject into auth activity
    @ContributesAndroidInjector(modules =
    [AuthViewModelModule::class, AuthFragmentBuilderModule::class,
        TaskFragmentModule::class, TaskViewModelModule::class,
        GoalFragmentModule::class, GoalsViewModelModule::class,
        NotesViewModelModule::class, NotesFragmentModule::class,
        CalendarFragmentModule::class, CalendarViewModelModule::class])
    abstract fun contributeMainActivity(): MainActivity

}