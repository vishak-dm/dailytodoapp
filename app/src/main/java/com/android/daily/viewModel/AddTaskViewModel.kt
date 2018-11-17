package com.android.daily.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.daily.repository.AddTaskRepository
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource

class AddTaskViewModel internal constructor(private val repository : AddTaskRepository) : ViewModel(){
    fun addTask(taskName: String, taskDescription: String, selectedDateInMills: Long,  goal_details: GoalsData) : MutableLiveData<Resource<Boolean>> {
        val goalId  = goal_details.gid
        return repository.addTaskToGoal(taskName,taskDescription,selectedDateInMills,goalId)
    }

}