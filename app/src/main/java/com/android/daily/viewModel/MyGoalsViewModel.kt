package com.android.daily.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.daily.repository.MyGoalsRepository
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource

class MyGoalsViewModel internal constructor(private val repository: MyGoalsRepository) : ViewModel() {

    fun getMyGoals(): MutableLiveData<Resource<List<GoalsData>>> {
        return repository.getMyGoals()
    }
}