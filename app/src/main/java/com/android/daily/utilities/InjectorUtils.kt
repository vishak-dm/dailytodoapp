package com.android.daily.utilities

import com.android.daily.repository.AddGoalsRepository
import com.android.daily.repository.MyGoalsRepository
import com.android.daily.repository.TodayTaskRepository
import com.android.daily.repository.UserRepository
import com.android.daily.viewModel.AddGoalsViewModelFactory
import com.android.daily.viewModel.AuthenticationViewModelFactory
import com.android.daily.viewModel.MyGoalsViewModelFactory
import com.android.daily.viewModel.TodayTasksViewModelFactory


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
}
