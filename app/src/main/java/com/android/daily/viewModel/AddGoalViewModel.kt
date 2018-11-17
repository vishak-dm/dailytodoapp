package com.android.daily.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.daily.repository.AddGoalsRepository
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource

class AddGoalViewModel internal constructor(private val repository: AddGoalsRepository) : ViewModel() {
    fun saveGoal(goal :GoalsData): MutableLiveData<Resource<Boolean>> {
        return repository.addGoal(goal)
    }

}