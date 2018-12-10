package com.android.daily.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.daily.repository.AddTaskRepository
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource

class AddTaskViewModel internal constructor(private val repository : AddTaskRepository) : ViewModel(){
    fun addTask(taskName: String, taskDescription: String, selectedDateInMills: Long,  goalId: String) : MutableLiveData<Resource<Boolean>> {
        return repository.addTaskToGoal(taskName,taskDescription,selectedDateInMills,goalId)
    }

    fun getAllGoalsForUser() : MutableLiveData<Resource<List<GoalsData>>> {
        return repository.getAllGoals()
    }


}