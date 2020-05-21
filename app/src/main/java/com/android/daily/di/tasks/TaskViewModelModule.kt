package com.android.daily.di.tasks

import androidx.lifecycle.ViewModel
import com.android.daily.di.ViewModelKey
import com.android.daily.viewModel.AddTaskViewModel
import com.android.daily.viewModel.TaskDetailsViewModel
import com.android.daily.viewModel.TaskTimerViewModel
import com.android.daily.viewModel.TodayTasksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TaskViewModelModule {
    //add today task view model to the map
    @Binds
    @IntoMap
    @ViewModelKey(TodayTasksViewModel::class)
    abstract fun bindTodayTaskViewModel(todayTasksViewModel: TodayTasksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    abstract fun bindAddTaskViewModel(addTaskViewModel: AddTaskViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskDetailsViewModel::class)
    abstract fun bindTaskDetailsViewModel(taskDetailsViewModel: TaskDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskTimerViewModel::class)
    abstract fun bindTaskTimerViewModel(taskTimerViewModel: TaskTimerViewModel): ViewModel
}