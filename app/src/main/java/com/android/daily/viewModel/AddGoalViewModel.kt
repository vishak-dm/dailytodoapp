package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.AddGoalsRepository
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource
import javax.inject.Inject

class AddGoalViewModel @Inject constructor(private val repository: AddGoalsRepository) : ViewModel() {
    fun saveGoal(goal :GoalsData): MutableLiveData<Resource<Boolean>> {
        return repository.addGoal(goal)
    }

}