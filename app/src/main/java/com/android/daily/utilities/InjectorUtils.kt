package com.android.daily.utilities

import com.android.daily.repository.*
import com.android.daily.viewModel.*


object InjectorUtils {
    private fun getUserRepository(): UserRepository {
        return UserRepository.getInstance()
    }

    fun provideAuthenticationViewModelFactory(): AuthenticationViewModelFactory {
        return AuthenticationViewModelFactory(getUserRepository())
    }

    private fun getTodayTaskRepository(): TodayTaskRepository {
        return TodayTaskRepository.getInstance()
    }

    fun provideTodayTaskViewModelFactory(): TodayTasksViewModelFactory {
        return TodayTasksViewModelFactory(getTodayTaskRepository())
    }

    private fun getAddGoalsRepository(): AddGoalsRepository {
        return AddGoalsRepository.getInstance()
    }

    fun provideAddGoalsViewModelFactory(): AddGoalsViewModelFactory {
        return AddGoalsViewModelFactory(getAddGoalsRepository())
    }


    private fun getMyGoalsRepository(): MyGoalsRepository {
        return MyGoalsRepository.getInstance()
    }

    fun provideMyGoalsViewModelFactory(): MyGoalsViewModelFactory {
        return MyGoalsViewModelFactory(getMyGoalsRepository())
    }

    private fun getAddTaskRepository(): AddTaskRepository {
        return AddTaskRepository.getInstance()
    }

    fun provideAddTaskViewModelFactory(): AddTaskViewModelFactory {
        return AddTaskViewModelFactory(getAddTaskRepository())
    }


    private fun getGoalDetailsRepository(): GoalDetailsRepository {
        return GoalDetailsRepository.getInstance()
    }

    fun provideGoalDetailsViewModelFactory(): GoalDetailsViewModelFactory {
        return GoalDetailsViewModelFactory(getGoalDetailsRepository())
    }
}
