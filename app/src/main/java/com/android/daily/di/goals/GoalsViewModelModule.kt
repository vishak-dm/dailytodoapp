package com.android.daily.di.goals

import androidx.lifecycle.ViewModel
import com.android.daily.di.ViewModelKey
import com.android.daily.viewModel.AddGoalViewModel
import com.android.daily.viewModel.GoalDetailsViewModel
import com.android.daily.viewModel.MyGoalsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GoalsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyGoalsViewModel::class)
    abstract fun bindMyGoalViewModel(myGoalsViewModel: MyGoalsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(AddGoalViewModel::class)
    abstract fun bindAddGoalViewModel(addGoalViewModel: AddGoalViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GoalDetailsViewModel::class)
    abstract fun bindGoalDetailsViewModel(goalDetailsViewModel: GoalDetailsViewModel): ViewModel

}