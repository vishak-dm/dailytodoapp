package com.android.daily.di.tasks

import com.android.daily.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TaskFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeTodaytaskFragment(): TodayTaskFragment

    @ContributesAndroidInjector
    abstract fun contributeAddTaskFragment() : AddTaskFragment

    @ContributesAndroidInjector
    abstract fun addTodayTasksFragment():AddTodayTasksFragment

    @ContributesAndroidInjector
    abstract  fun contributeTaskDetailsFragment() : TaskDetailsFragment

    @ContributesAndroidInjector
    abstract  fun contributeSeesionFragment() : TaskSessionFragment

}