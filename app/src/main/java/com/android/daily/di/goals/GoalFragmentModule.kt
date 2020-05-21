package com.android.daily.di.goals

import com.android.daily.ui.GoalDateFragment
import com.android.daily.ui.MyGoalsDetailsFragment
import com.android.daily.ui.MyGoalsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class GoalFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeGoalsFragment(): MyGoalsFragment

    @ContributesAndroidInjector
    abstract fun contributeGoalsdateFragment(): GoalDateFragment

    @ContributesAndroidInjector
    abstract fun contributeGoalsdetailseFragment(): MyGoalsDetailsFragment
}