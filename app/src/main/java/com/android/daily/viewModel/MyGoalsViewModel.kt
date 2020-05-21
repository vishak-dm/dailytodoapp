package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.MyGoalsRepository
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource
import javax.inject.Inject

class MyGoalsViewModel @Inject constructor(private val repository: MyGoalsRepository) : ViewModel() {

    fun getMyGoals(): MutableLiveData<Resource<List<GoalsData>>> {
        return repository.getMyGoals()
    }
}