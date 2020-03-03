package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.GoalDetailsRepository
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource

class GoalDetailsViewModel internal constructor(private val repository : GoalDetailsRepository) : ViewModel(){
    fun getTasks(goalId: String) : MutableLiveData<Resource<List<TaskData>>>{
        return repository.getTasksForGoal(goalId)
    }

}